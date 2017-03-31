import m from 'mithril';
import stream from 'mithril/stream';
import SettingsVM from '../../../models/pages/settings';
import TextBox from '../../forms/textbox/textbox';
import Button from '../../forms/button/button';

const settingsVM = new SettingsVM();

const Settings = {
  oninit() {
    this.vm = settingsVM;
  },

  view() {
    return m('.SettingsPage', [
      m('h1.PageHeading', 'Settings'),
      m(TextBox, {
        description: 'Select a directory to use for syncing files:',
        value: this.syncDir,
        onchange: e => this.vm.syncDir(e.target.value),
      }),
      m(Button, {
        value: 'Set',
        onclick: () => this.vm.save(),
      }),
    ]);
  },
};

export default Settings;
