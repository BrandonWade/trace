package main

import (
	"bytes"
	"encoding/base64"
	"fmt"
	"io/ioutil"
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
	done := false

	for !done {
		message := &Message{}
		conn.ReadJSON(message)

		switch message.Type {
		case "part":
			body := message.Body[:message.Length]
			data, _ := base64.RawStdEncoding.DecodeString(body)
			buffer.WriteString(string(data))
		case "done":
			data := []byte(buffer.String())
			_ = ioutil.WriteFile("C:\\Users\\Brandon\\Desktop\\files\\output.m4a", data, 0644)
			conn.Close()
			done = true
		}
	}
}
