package lib

import (
	"fmt"
	"log"
	"net/url"

	"github.com/gin-gonic/gin"

	"github.com/gorilla/websocket"
)

// BufferSize - read / write buffer size
const BufferSize = 4096

// Connection - used to read from a websocket
type Connection struct {
	Conn *websocket.Conn
}

// NewConnection - initializes a Connection object
func NewConnection() *Connection {
	return &Connection{}
}

// Open - opens a Connection
func (c *Connection) Open(context *gin.Context) {
	req := context.Request
	res := context.Writer

	var upgrader = websocket.Upgrader{
		ReadBufferSize:  BufferSize,
		WriteBufferSize: BufferSize,
	}

	conn, err := upgrader.Upgrade(res, req, nil)
	if err != nil {
		log.Println("Failed to create websocket.")
		log.Println(err)
	}

	c.Conn = conn
}

// Dial - connects to a websocket on a server
func (c *Connection) Dial(host string) {
	url := url.URL{Scheme: "ws", Host: "localhost:8080"}
	conn, _, err := websocket.DefaultDialer.Dial(url.String(), nil)
	if err != nil {
		fmt.Println(err)
	}

	c.Conn = conn
}

// Read - reads from a Connection
func (c *Connection) Read() *Message {
	message := &Message{}
	c.Conn.ReadJSON(message)

	return message
}

// Write - writes to a Connection
func (c *Connection) Write(message *Message) {
	c.Conn.WriteJSON(message)
}

// Close - close the Connection
func (c *Connection) Close() {
	c.Conn.WriteMessage(websocket.CloseNormalClosure, []byte(""))
	c.Conn.Close()
}
