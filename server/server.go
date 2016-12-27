package main

import (
	"bufio"
	"encoding/base64"
	"math"
	"net/http"
	"os"
	"strings"
	"sync"

	"github.com/BrandonWade/trace/lib"
	"github.com/gin-gonic/gin"
)

// The number of goroutines to complete
var waitGroup sync.WaitGroup

// The list of new files to send to the client
var newFiles []lib.File

// TODO: Use real dir
var dir = "C:\\Users\\Brandon\\Music"

func main() {
	g := gin.Default()
	g.GET("/", runServer)
	g.GET("/file", sendFile)
	g.GET("/ping", handlePing)
	g.Run(":8080")
}

func runServer(c *gin.Context) {
	conn := lib.NewConnection()
	defer conn.Close()

	// Open the Connection
	conn.Open(c)

	// TODO: Use real dirs
	ignoreDirs := []string{
		"C:\\Users\\Brandon\\Music\\Playlists",
		"C:\\Users\\Brandon\\Music\\iTunes\\Album Artwork",
		"C:\\Users\\Brandon\\Music\\iTunes\\iTunes Media\\Automatically Add to iTunes",
		"C:\\Users\\Brandon\\Music\\iTunes\\iTunes Media\\Mobile Applications",
		"C:\\Users\\Brandon\\Music\\iTunes\\Previous iTunes Libraries",
		"C:\\Users\\Brandon\\Music\\iTunes\\Ringtones",
		"C:\\Users\\Brandon\\Music\\iTunes\\sentinel",
		"C:\\Users\\Brandon\\Music\\iTunes\\iTunes Library Extras.itdb",
		"C:\\Users\\Brandon\\Music\\iTunes\\iTunes Library Genius.itdb",
		"C:\\Users\\Brandon\\Music\\iTunes\\iTunes Library.itl",
		"C:\\Users\\Brandon\\Music\\iTunes\\iTunes Music Library.xml",
	}

	newFiles = []lib.File{}
	clientFiles := make(map[string]bool)
	done := false

	// Get a list of file names from the client
	for !done {
		message := conn.Read()

		switch message.Type {
		case lib.List:
			relPath := strings.Replace(message.File, "/", "\\", -1)
			clientFiles[relPath] = true
		case lib.Done:
			done = true
		}
	}

	// Get a list of local files
	localFiles := lib.Scan(dir, ignoreDirs)

	// Compare the local and client files to build a naive one-way diff
	for file := range localFiles {
		if !clientFiles[file] {
			newFiles = append(newFiles, localFiles[file])
		}
	}

	waitGroup.Add(len(newFiles))

	// Send the list of new files to the client
	for _, file := range newFiles {
		message := &lib.Message{Type: lib.List, File: file.RelPath, Length: 1, Body: ""}
		conn.Write(message)
	}

	// Indicate the end of the list of new files
	message := &lib.Message{Type: lib.Done, File: "", Length: -1, Body: ""}
	conn.Write(message)
}

// SendFile - sends a file to the client
func sendFile(c *gin.Context) {
	conn := lib.NewConnection()
	defer conn.Close()

	conn.Open(c)
	fileMessage := conn.Read()
	fileName := fileMessage.File

	filePtr, _ := os.Open(dir + fileName)
	defer filePtr.Close()

	// Notify the client of the new file
	fileStat, _ := filePtr.Stat()
	fileSize := fileStat.Size()
	numParts := math.Ceil(float64(fileSize) / float64(lib.BufferSize))

	newMessage := &lib.Message{Type: lib.New, File: fileName, Length: int(numParts), Body: ""}
	conn.Write(newMessage)

	buffer := bufio.NewReader(filePtr)

	for {
		buff := make([]byte, lib.BufferSize)
		_, err := buffer.Read(buff)
		if err != nil {
			break
		}

		// Encode the data segment and send it
		body := base64.RawStdEncoding.EncodeToString(buff)
		message := &lib.Message{Type: lib.Part, File: fileName, Length: len(body), Body: body}
		conn.Write(message)
	}

	// Send a done message
	message := &lib.Message{Type: lib.Done, File: fileName, Length: 0, Body: ""}
	conn.Write(message)

	// Indicate that this file has been sent
	waitGroup.Done()
}

func handlePing(c *gin.Context) {
	c.Data(http.StatusOK, gin.MIMEHTML, nil)
}
