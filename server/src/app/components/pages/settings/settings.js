import m from 'mithril';
import stream from 'mithril/stream';
import TextBox from '../../forms/textbox/textbox';
import Button from '../../forms/button/button';

const Settings = {
  oninit(vn) {
    this.syncDir = stream(localStorage.getItem('trace.sync.dir') || '');
  },

  view() {
    return m('.SettingsPage', [
      m('h1.PageHeading', 'Settings'),
      m(TextBox, {
        description: 'Select a directory to use for syncing files:',
        value: this.syncDir,
        onchange: e => this.syncDir(e.target.value),
      }),
      m(Button, {
        value: 'Set',
        onclick: () => localStorage.setItem('trace.sync.dir', this.syncDir),
      }),
    ]);
  },
};

export default Settings;
