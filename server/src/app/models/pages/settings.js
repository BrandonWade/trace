import stream from 'mithril/stream';

class Settings {
  constructor() {
    const syncDir = localStorage.getItem('trace.sync.dir') || '';
    this.syncDir = stream(syncDir);
    updateServer();
  }

  save() {
    localStorage.setItem('trace.sync.dir', this.syncDir);
    updateServer();
  }

  updateServer() {
    fetch('/update/dir', {
      method: 'POST',
      body: JSON.stringify(this.syncDir),
    });
  }
};

export default Settings;
