class Settings {
  constructor() {
    this.syncDir = window.settings.Dir;
  }

  save() {
    const dir = { dir: this.syncDir };
    const headers = new Headers({
      'Content-Type' : 'application/json',
    });

    fetch('/settings/update/dir', {
      method: 'POST',
      headers: headers,
      body: JSON.stringify(dir),
    });
  }
};

export default Settings;
