import stream from 'mithril/stream';

class Settings {
  constructor() {
    const syncDir = localStorage.getItem('trace.sync.dir') || '';
    this.syncDir = stream(syncDir);
    this.updateServer();
  }

  save() {
    localStorage.setItem('trace.sync.dir', this.syncDir);
    this.updateServer();
  }

  updateServer() {
    const dir = { dir: this.syncDir };
    const headers = new Headers({
      'Content-Type' : 'application/json',
    });

    fetch('/update/dir', {
      method: 'POST',
      headers: headers,
      body: JSON.stringify(dir),
    });
  }
};

export default Settings;
