import React, { Component } from 'react';
import { connect } from 'react-redux';
import TextBox from '../components/textbox/Textbox';
import Button from '../components/button/Button';
import {
    fetchAction,
    interfaceAction,
    UPDATE_NEW_DIR,
    SET_SYNC_DIR,
    SAVE_SETTINGS,
} from '../actions/index';

class Settings extends Component {
    constructor(props) {
        super(props);

        this.setSyncDir = this.setSyncDir.bind(this);
    }

    setSyncDir() {
        this.props.setSyncDir(this.props.newDir);
        this.props.saveDir(this.props.newDir);
    }

    render() {
        return (
            <div className='SettingsPage'>
                <h1 className='Page-heading'>Settings</h1>
                <section className='Page-section'>
                    <TextBox
                        description='Enter a directory to use for syncing files:'
                        handleChange={this.props.updateNewDir}
                    />
                    <Button
                        value='Set'
                        handleClick={this.setSyncDir}
                    />
                </section>
                <section className='Page-section'>
                    <p>{`Current directory: ${this.props.syncDir}`}</p>
                </section>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        syncDir: state.syncDir,
        newDir: state.newDir,
    };
};

const mapDispatchToProps = (dispatch) => {
    return {
        updateNewDir(evt) {
            dispatch(interfaceAction(UPDATE_NEW_DIR, evt.target.value));
        },
        setSyncDir(dir) {
            dispatch(interfaceAction(SET_SYNC_DIR, dir));
        },
        saveDir(dir) {
            dispatch(fetchAction(SAVE_SETTINGS, { dir }));
        },
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
