import React, { Component } from 'react';
import TextBox from '../components/textbox/Textbox';
import Button from '../components/button/Button';

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
        const dir = {dir: this.state.newSyncDir};
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
            <div className={'SettingsPage'}>
                <h1 className={'Page-heading'}>Settings</h1>
                <section className={'Page-section'}>
                    <TextBox description={'Select a directory to use for syncing files:'}
                             handleChange={e => this.updateState(e.target.value, 'newSyncDir')} />
                    <Button value={'Set'}
                            handleClick={() => this.updateState(this.state.newSyncDir, 'syncDir', this.save)} />
                </section>
                <section className={'Page-section'}>
                    <p>{`Current directory: ${this.state.syncDir}`}</p>
                </section>
            </div>
        );
    }
};

export default Settings;
