import m from 'mithril';
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
      m('div', [
        m(TextBox, {
          description: 'Select a directory to use for syncing files:',
          onchange: e => this.vm.newSyncDir = e.target.value,
        }),
        m(Button, {
          value: 'Set',
          onclick: () => this.vm.save(),
        }),
      ]),
      m('p', `Current directory: ${this.vm.syncDir}`)
    ]);
  },
};

export default Settings;
