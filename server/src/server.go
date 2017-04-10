package main

import (
	"bufio"
	"log"
	"math"
	"net/http"
	"os"
	"strings"
	"sync"

	"github.com/gin-gonic/gin"
)

// The list of new files to send to the client
var newFiles []File

var dir string
var filters = []string{}

var dirMutex sync.Mutex
var filterMutex sync.Mutex

var conn *Connection

func main() {
	g := gin.Default()

	g.LoadHTMLFiles("./app/app.html")
	g.Static("dist/", "./app/dist")

	g.GET("/", index)
	g.GET("/sync", syncFiles)
	g.GET("/file", sendFile)
	g.POST("/update/dir", updateSyncDir)
	g.POST("/update/filters", updateFilterList)
	g.Run(":8080")
}

// index - Returns the index page to the client to display
func index(c *gin.Context) {
	c.HTML(http.StatusOK, "app.html", nil)
}

// syncFiles - Determine new files on the server
func syncFiles(c *gin.Context) {
	conn = NewConnection()

	// Open the Connection
	conn.Open(c)

	for {
		newFiles = []File{}
		clientFiles := make(map[string]bool)
		done := false

		// Get a list of file names from the client
		for !done {
			message := conn.Read()

			switch message.Type {
			case List:
				relPath := strings.Replace(message.File, "/", "\\", -1)
				clientFiles[relPath] = true
			case Done:
				done = true
			}
		}

		// Get a list of local files
		localFiles := Scan(dir, filters)

		// Compare the local and client files to build a naive one-way diff
		for file := range localFiles {
			if !clientFiles[file] {
				newFiles = append(newFiles, localFiles[file])
			}
		}

		// Send the list of new files to the client
		for _, file := range newFiles {
			message := &Message{Type: List, File: file.RelPath, Length: 1, Body: ""}
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

	filePtr, _ := os.Open(dir + fileName)
	defer filePtr.Close()

	// Notify the client of the new file
	fileStat, _ := filePtr.Stat()
	fileSize := fileStat.Size()
	numParts := math.Ceil(float64(fileSize) / float64(BufferSize))

	newMessage := &Message{Type: New, File: fileName, Length: int(numParts), Body: ""}
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

// updateSyncDir - sets the sync directory to the directory received from the client
func updateSyncDir(c *gin.Context) {
	newDir := &SyncDir{}
	err := c.Bind(newDir)
	if err != nil {
		log.Println("Failed to bind sync directory.")
		log.Println(err)
		return
	}

	// newDir.Dir = strings.Replace(newDir.Dir, "\\\\", "\\", -1)

	dirMutex.Lock()
	dir = newDir.Dir
	dirMutex.Unlock()
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

	filterMutex.Lock()
	filters = newFilters.Filters
	filterMutex.Unlock()
}
