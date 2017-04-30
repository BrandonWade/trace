class Settings {
  constructor() {
    this.syncDir = localStorage.getItem('trace.sync.dir') || '';
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
