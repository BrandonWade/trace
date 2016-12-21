package lib

const (
	// New - used when sending a new file
	New = "new"

	// Count - used when sending the number of files
	Count = "count"

	// List - used when sending the file as part of a list
	List = "list"

	// ListComplete - when receiving the last item in a list
	ListComplete = "list_complete"

	// Part - used when sending a segment of a file
	Part = "part"

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
