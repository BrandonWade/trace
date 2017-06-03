package main

import (
	"bufio"
	"encoding/json"
	"html/template"
	"log"
	"math"
	"net"
	"net/http"
	"os"
	"regexp"
	"strconv"
	"strings"
	"sync"

	"github.com/gin-gonic/gin"
	"github.com/nanobox-io/golang-scribble"
)

// The directory used to hold temporary information for Trace
var settingsDir = os.TempDir() + "\\trace"

// The list of new files to send to the client
var newFiles []File

// The sync directory and list of filters
var settings Settings

// Mutex to prevent variable updates during a sync
var settingsMutex sync.Mutex

// The connection to the client
var conn *Connection

func main() {
	readSettings()
	g := gin.Default()

	g.LoadHTMLFiles("./app/app.tmpl")
	g.Static("dist/", "./app/dist")

	g.GET("/", index)
	g.GET("/sync", syncFiles)
	g.GET("/file", sendFile)
	g.POST("/settings/update/dir", updateSyncDir)
	g.POST("/settings/update/filters", updateFilterList)
	g.Run(":8080")
}

// readSettings - read the settings file from the database if it exists
func readSettings() {
	db, err := scribble.New(settingsDir, nil)
	if err != nil {
		log.Println("Failed to create settings database driver")
		log.Println(err)
	}

	if _, err := os.Stat(settingsDir + "\\settings\\settings.json"); os.IsNotExist(err) {
		settings = Settings{}
		return
	}

	if err = db.Read("settings", "settings", &settings); err != nil {
		log.Println("Failed to read settings from database")
		log.Println(err)
	}
}

// writeSettings - write the settings file to the database
func writeSettings() {
	_ = os.Mkdir(settingsDir, 0700)

	db, err := scribble.New(settingsDir, nil)
	if err != nil {
		log.Println("Failed to create settings database driver")
		log.Println(err)
	}

	if err := db.Write("settings", "settings", settings); err != nil {
		log.Println("Failed to save settings to database")
		log.Println(err)
	}
}

// index - Returns the index page to the client to display
func index(c *gin.Context) {
	settingsBytes, err := json.Marshal(settings)
	if err != nil {
		log.Println("Failed to marshal settings struct")
		log.Println(err)
	}

	c.HTML(http.StatusOK, "app.tmpl", gin.H{
		"settings": template.URL(string(settingsBytes)),
		"ip": getLocalIP(),
	})
}

// syncFiles - Calculate new files on the server
func syncFiles(c *gin.Context) {
	conn = NewConnection()
	conn.Open(c)

	for {
		newFiles = []File{}
		clientFiles := make(map[string]bool)
		done := false

		// Get a list of file names from the client
		for !done {
			message := conn.Read()

			switch message.Type {
			case New:
				relPath := strings.Replace(message.File, "/", "\\", -1)
				clientFiles[relPath] = true
			case Done:
				done = true
			}
		}

		// Get a list of local files
		settingsMutex.Lock()
		localFiles := Scan(settings.Dir, settings.Filters)
		settingsMutex.Unlock()

		// Compare the local and client files to build a naive one-way diff
		for file := range localFiles {
			if !clientFiles[file] {
				newFiles = append(newFiles, localFiles[file])
			}
		}

		// Send the list of new files to the client
		for _, file := range newFiles {
			message := &Message{Type: New, File: file.RelPath, Body: ""}
			conn.Write(message)
		}

		// Indicate the end of the list of new files
		conn.WriteDone()
	}
}

// sendFile - sends a file to the client
func sendFile(c *gin.Context) {
	conn := NewConnection()
	defer conn.Close()

	conn.Open(c)
	fileMessage := conn.Read()
	fileName := fileMessage.File

	settingsMutex.Lock()
	filePtr, _ := os.Open(settings.Dir + fileName)
	defer filePtr.Close()
	settingsMutex.Unlock()

	// Notify the client of the new file
	fileStat, _ := filePtr.Stat()
	fileSize := fileStat.Size()
	numParts := math.Ceil(float64(fileSize) / float64(BufferSize))

	newMessage := &Message{Type: New, File: fileName, Body: strconv.Itoa(int(numParts))}
	conn.Write(newMessage)

	buffer := bufio.NewReader(filePtr)

	for {
		buff := make([]byte, BufferSize)
		_, err := buffer.Read(buff)
		if err != nil {
			break
		}

		// Send the current file segment
		conn.WriteBinary(buff)
	}

	// Send a done message
	conn.WriteDone()
}

// getLocalIP - returns the local IP address (192.168.X.X)
func getLocalIP() string {
	addrs, err := net.InterfaceAddrs()
	if err != nil {
		log.Println("Failed to retrieve network interface addresses.")
		log.Println(err)
		return ""
	}

	re := regexp.MustCompile("192\\.168\\.\\d\\d?\\d?\\.\\d\\d?\\d?")
	for _, addr := range addrs {
		ip := re.FindString(addr.String())
		if ip != "" {
			return ip
		}
	}

	return ""
}

// updateSyncDir - sets the sync directory to the directory received from the client
func updateSyncDir(c *gin.Context) {
	newDir := &SyncDir{}
	err := c.Bind(newDir)
	if err != nil {
		log.Println("Failed to bind sync directory.")
		log.Println(err)
		return
	}

	settingsMutex.Lock()
	settings.Dir = newDir.Dir
	writeSettings()
	settingsMutex.Unlock()
}

// updateFilterList - sets the filter list to the list received from the client
func updateFilterList(c *gin.Context) {
	newFilters := &FilterList{}
	err := c.Bind(newFilters)
	if err != nil {
		log.Println("Failed to update filter list.")
		log.Println(err)
		return
	}

	settingsMutex.Lock()
	settings.Filters = newFilters.Filters
	writeSettings()
	settingsMutex.Unlock()
}
