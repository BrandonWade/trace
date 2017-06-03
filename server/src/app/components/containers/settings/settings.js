import React, { Component } from 'react';
import TextBox from '../../forms/textbox/textbox';
import Button from '../../forms/button/button';

class Settings extends Component {
  constructor(props) {
    super(props);

    this.state = {
      syncDir: window.settings.Dir || '',
      newSyncDir: window.settings.Dir || '',
    };
  }

  updateState(value, field) {
    this.setState({
      ...this.state,
      field: value,
    });
  }

  save() {
    this.updateState(this.newSyncDir, 'syncDir');

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

  render() {
    return (
      <div className={ 'SettingsPage' }>
        <h1 className={ 'Page-Heading' }>Settings</h1>
        <section className={ 'Page-Section' }>
          <TextBox description={ 'Select a directory to use for syncing files:' } handleChange={ e => this.updateState(e.target.value, 'newSyncDir') } />
          <Button value={ 'Set' } handleClick={ () => this.save() } />
          <p>{ `Current directory: ${this.state.syncDir}` }</p>
        </section>
      </div>
    );
  }
};

export default Settings;
