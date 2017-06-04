import React, { Component } from 'react';
import TextBox from '../../components/forms/textbox/textbox';
import Button from '../../components/forms/button/button';

class Settings extends Component {
  constructor(props) {
    super(props);

    this.state = {
      syncDir: window.settings.Dir || '',
      newSyncDir: window.settings.Dir || '',
    };
  }

  updateState(value, field, callback = () => {}) {
    this.setState({
      ...this.state,
      [field]: value,
    }, callback);
  }

  save() {
    const dir = { dir: this.state.newSyncDir };
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
          <Button value={ 'Set' } handleClick={ () => this.updateState(this.state.newSyncDir, 'syncDir', this.save) } />
        </section>
        <section className={ 'Page-Section' }>
          <p>{ `Current directory: ${this.state.syncDir}` }</p>
        </section>
      </div>
    );
  }
};

export default Settings;
