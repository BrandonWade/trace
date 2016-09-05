package main

import (
	"bytes"
	"fmt"
	"io/ioutil"
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
		// message := &Message{}
		// conn.ReadJSON(message)
		t, contents, err := conn.ReadMessage()
		if err != nil {
			break
		}

		if t == websocket.BinaryMessage {
			log.Println("PART RECEIVED")
			// 	data := message.Body[:message.Length]
			// 	contents, _ := base64.RawStdEncoding.DecodeString(data)
			buffer.WriteString(string(contents))
		} else if t == websocket.CloseGoingAway {
			log.Println("CLOSE RECEIVED")
			break
		}
	}

	data := []byte(buffer.String())
	_ = ioutil.WriteFile("C:\\Users\\Brandon\\Desktop\\files\\output.mp3", data, 0644)
	conn.Close()

	// log.Printf("%+v", buffer.String())
}
