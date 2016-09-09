package main

import (
	"bufio"
	"encoding/base64"
	"log"
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

func main() {
	g := gin.Default()
	g.GET("/", runServer)
	g.Run(":8080")
}

func runServer(c *gin.Context) {
	conn := lib.NewConnection()
	defer conn.Close()

	// Open the Connection
	conn.Open(c)

	// TODO: Use a real dir
	dir := "C:\\Users\\Brandon\\Desktop\\files"

	newFiles = []lib.File{}
	clientFiles := make(map[string]bool)
	done := false

	// Get a list of file names from the client
	for !done {
		message := conn.Read()

		switch message.Type {
		case lib.New:
			relPath := strings.Replace(message.File, "/", "\\", -1)
			clientFiles[relPath] = true
		case lib.Done:
			done = true
		}
	}

	// Get a list of local files
	localFiles := lib.Scan(dir)

	// Compare the local and client files to build a naive one-way diff
	for file := range localFiles {
		if !clientFiles[file] {
			newFiles = append(newFiles, localFiles[file])
		}
	}

	waitGroup.Add(len(newFiles))

	countMessage := &lib.Message{Type: lib.Count, File: "", Length: len(newFiles), Body: ""}
	conn.Write(countMessage)

	for _, file := range newFiles {
		go sendFile(file, conn)
	}

	// Wait for all files to finish sending
	waitGroup.Wait()
}

// SendFile - sends a file to the client
func sendFile(file lib.File, conn *lib.Connection) {
	filePtr, _ := os.Open(file.Path)
	defer filePtr.Close()

	// Notify the client of the new file
	newMessage := &lib.Message{Type: lib.New, File: file.RelPath, Length: 0, Body: ""}
	log.Printf("Sending file %s...", file.Name)
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
		message := &lib.Message{Type: "part", File: file.RelPath, Length: len(body), Body: body}
		conn.Write(message)
	}

	// Send a done message
	message := &lib.Message{Type: "done", File: file.RelPath, Length: 0, Body: ""}
	conn.Write(message)

	// Indicate that this file has been segment
	waitGroup.Done()
}
