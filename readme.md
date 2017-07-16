# Trace
Sync files from your desktop to your Android phone in a flash. Trace uses a local server to send files over wifi to your phone through it's accompanying Android app.

## Getting Started
**IMPORTANT:** Trace uses your local network to transfer files. **This means that your desktop and phone need to be on the _same_ network in order to connect and sync**.

### Server setup
1. Install [Go](https://golang.org/)
2. `go build` the server
3. Run the server and navigate to `localhost:8080`
4. Under the `Settings` menu, enter the directory you wish to sync with your device
5. Optionally under the `Filters` menu, setup filters for files & folders you want trace to ignore

### Client setup
1. Install the Android app on your phone using [Android Studio](https://developer.android.com/studio/index.html)
2. Launch the app and open the `Connection` menu
3. Enter the local IP address of the machine running an instance of the server (this can be found on the `Home` screen of the server UI)
4. Optionally set the maximum number of concurrent connections
5. Open the `Settings` menu and enter the path to the folder on your phone you want to sync with your desktop

### Syncing
1. In the app, press the sync button (Note: it will only be enabled if the server can be reached)
2. When the sync completes, you will be presented with a list of files to download
3. Select the desired files and press the confirm button

## Notes
* Trace uses a naive method to detect new files - only _new_ files (that is, files that exist on your desktop but not on your device) will be detected and downloaded. At this time, modified files are not detected.
* With the diff approach Trace uses, files can only be copied _from the server to the client_. At this time, there is no way to copy files from the client to the server.
* Only a single directory (and all of it's subdirectories) can be synced with Trace at a time.
* Only the client may trigger a sync. The server cannot initiate a sync.
