package main

const (
	// New - used when sending a new file
	New = "new"

	// Done - used to indicate the end of a series of messages
	Done = "done"
)

// Message - Contains information sent over a Connection
type Message struct {
	Type string
	File string
	Body string
}

// Settings - Contains information used when syncing
type Settings struct {
	Dir     string
	Filters []string
}

// SyncDir - Contains a directory sent to the server
type SyncDir struct {
	Dir string `json:"dir"`
}

// FilterList - Contains a list of filters sent to the server
type FilterList struct {
	Filters []string `json:"filters"`
}
