package main

import (
	"bytes"
	"encoding/base64"
	"io/ioutil"
	"log"
	"os"
	"strings"
	"sync"

	"github.com/BrandonWade/trace/lib"
)

// The number of goroutines to complete
var waitGroup sync.WaitGroup

func main() {
	// TODO: Use a real host & dir
	host := "localhost:8080"
	dir := "C:\\Users\\Brandon\\Desktop\\clientFiles"

	clientFiles := lib.Scan(dir)

	conn := lib.NewConnection()
	conn.Dial(host)
	defer conn.Close()

	// Send the RelPath of each file to the server
	for _, file := range clientFiles {
		message := &lib.Message{Type: lib.New, File: file.RelPath, Length: 0, Body: ""}
		conn.Write(message)
	}

	// Notify the server that there are no more files incoming
	doneMessage := &lib.Message{Type: lib.Done, File: "", Length: 0, Body: ""}
	conn.Write(doneMessage)

	// The number & list of new files coming from the server
	numFiles := 0
	newFiles := make(map[string]*bytes.Buffer)

	// The number of files received
	receivedFiles := 0

	var buffer bytes.Buffer
	done := false

	for !done {
		message := conn.Read()

		// Determine the action based on file type
		switch message.Type {
		case lib.New:
			newFiles[message.File] = new(bytes.Buffer)
		case lib.Count:
			numFiles = message.Length
			waitGroup.Add(numFiles)
		case lib.Part:
			body := message.Body[:message.Length]
			data, _ := base64.RawStdEncoding.DecodeString(body)
			buffer.WriteString(string(data))
		case lib.Done:
			if message.File != "" {
				receivedFiles++
				go writeFile(dir, message.File, newFiles[message.File])
			}
		}

		if receivedFiles == numFiles {
			done = true
		}
	}

	// Wait until all files have been written to disk
	waitGroup.Wait()
}

// WriteFile - write the completed file to disk
func writeFile(base, path string, contents *bytes.Buffer) {
	// TODO: Implement me / port over trace-client method
	defer waitGroup.Done()

	// Create any directories needed
	dirs := path[:strings.LastIndex(path, "\\")]
	err := os.MkdirAll(base+dirs, 0700)
	if err != nil {
		log.Println("Failed to create directores: " + path)
		log.Println(err)
	}

	data := []byte(contents.String())
	ioutil.WriteFile(base+path, data, 0700)

	// OLD WAY FOR A SINGLE FILE
	// data := []byte(buffer.String())
	// _ = ioutil.WriteFile("C:\\Users\\Brandon\\Desktop\\files\\"+message.File, data, 0644)
}
