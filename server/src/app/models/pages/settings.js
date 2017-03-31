import stream from 'mithril/stream';

class Settings {
  constructor() {
    const syncDir = localStorage.getItem('trace.sync.dir') || '';
    this.syncDir = stream(syncDir);
  }

  save() {
    localStorage.setItem('trace.sync.dir', this.syncDir);
  }
};

export default Settings;
