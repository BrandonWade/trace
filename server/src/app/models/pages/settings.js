import stream from 'mithril/stream';

class Settings {
  constructor() {
    const syncDir = localStorage.getItem('trace.sync.dir') || '';
    this.syncDir = stream(syncDir);
  }

  save() {
    localStorage.setItem('trace.sync.dir', this.syncDir);
    fetch('/update/sync', {
      method: 'POST',
      body: JSON.stringify(this.syncDir),
    });
  }
};

export default Settings;
