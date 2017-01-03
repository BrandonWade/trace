package lib

const (
	// New - used when sending a new file
	New = "new"

	// List - used when sending the file as part of a list
	List = "list"

	// Done - used when there are no more file parts to send
	Done = "done"
)

// Message - Used to contain information sent over the wire
type Message struct {
	Type   string
	File   string
	Length int
	Body   string
}
