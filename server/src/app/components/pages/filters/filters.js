import m from 'mithril';
import stream from 'mithril/stream';
import FiltersVM from '../../../models/pages/filters';
import TextBox from '../../forms/textbox/textbox';
import Button from '../../forms/button/button';
import Select from '../../forms/select/select';

const filtersVM = new FiltersVM();

const Filter = {
  oninit() {
    this.vm = filtersVM;
  },

  view() {
    return m('.FiltersPage', [
      m('h1.PageHeading', 'Filters'),
      m(TextBox, {
        description: 'New filter:',
        onchange: e => this.vm.newFilter(e.target.value),
      }),
      m(Button, {
        value: 'Add',
        onclick: () => this.vm.add(),
      }),
      m(Select, {
        description: 'Existing filters:',
        options: this.vm.filters(),
        onchange: e => this.vm.selectedFilterIndex(e.target.selectedIndex),
      }),

      m(Button, {
        value: 'Remove',
        onclick: () => this.vm.remove(),
      })
    ]);
  },
};

export default Filter;
