package lib

import (
	"fmt"
	"log"
	"net/url"
	"sync"

	"github.com/gin-gonic/gin"

	"github.com/gorilla/websocket"
)

// BufferSice - read / write buffer size
const BufferSize = 4096

// Connection - used to read from a websocket
type Connection struct {
	Conn  *websocket.Conn
	Mutex *sync.RWMutex
}

// NewConnection - initializes a Connection object
func NewConnection() *Connection {
	return &Connection{Mutex: new(sync.RWMutex)}
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
	c.Mutex.RLock()
	defer c.Mutex.RUnlock()

	message := &Message{}
	c.Conn.ReadJSON(message)

	return message
}

// Write - writes to a Connection
func (c *Connection) Write(message *Message) {
	c.Mutex.Lock()
	defer c.Mutex.Unlock()

	c.Conn.WriteJSON(message)
}

// Close - close the Connection
func (c *Connection) Close() {
	c.Conn.WriteMessage(websocket.CloseNormalClosure, []byte(""))
	c.Conn.Close()
}
