package main

import (
	"bufio"
	"log"
	"os"

	"github.com/gin-gonic/gin"
	"github.com/gorilla/websocket"
)

func main() {
	g := gin.Default()
	g.GET("/", runServer)
	g.Run(":8080")
}

// Message ...
type Message struct {
	Type   string
	File   string
	Length int
	Body   string
}

func runServer(c *gin.Context) {
	var conn *websocket.Conn

	req := c.Request
	res := c.Writer

	var upgrader = websocket.Upgrader{
		ReadBufferSize:  1024,
		WriteBufferSize: 1024,
	}

	conn, err := upgrader.Upgrade(res, req, nil)
	if err != nil {
		log.Println("Failed to create websocket.")
		log.Println(err)
	}

	// Open the file and create a buffer
	filePtr, _ := os.Open("C:\\Users\\Brandon\\Desktop\\files\\file1.txt")
	defer filePtr.Close()
	buffer := bufio.NewReader(filePtr)

	for {
		part := make([]byte, 2048)
		length, err := buffer.Read(part)
		if err != nil {
			break
		}

		filePart := &Message{Type: "part", File: "files\\file1.txt", Length: length - 1, Body: string(part)}
		conn.WriteJSON(filePart)
	}

	doneMessage := &Message{Type: "done", File: "files\\file1.txt", Length: 0, Body: ""}
	conn.WriteJSON(doneMessage)
}
