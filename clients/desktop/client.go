package main

import (
	"bytes"
	"fmt"
	"log"
	"net/url"

	"github.com/gorilla/websocket"
)

// Message ...
type Message struct {
	Type   string
	File   string
	Length int
	Body   string
}

func main() {
	url := url.URL{Scheme: "ws", Host: "localhost:8080"}
	conn, _, err := websocket.DefaultDialer.Dial(url.String(), nil)
	if err != nil {
		fmt.Println(err)
	}

	var buffer bytes.Buffer

	for {
		message := &Message{}
		conn.ReadJSON(message)

		if message.Type == "part" {
			contents := message.Body[:message.Length]
			buffer.WriteString(contents)
		} else if message.Type == "done" {
			break
		}
	}

	log.Printf("%+v", buffer.String())
}
