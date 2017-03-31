import m from 'mithril';
import stream from 'mithril/stream';
import TextBox from '../../forms/textbox/textbox';
import Button from '../../forms/button/button';
import Select from '../../forms/select/select';

const Filter = {
  saveFilters() {
    localStorage.setItem('trace.filters.ignore', JSON.stringify(this.filters()));
  },

  oninit() {
    const filters = JSON.parse(localStorage.getItem('trace.filters.ignore')) || [];
    this.filters = stream(filters);
    this.newFilter = stream();
    this.selectedFilterIndex = stream();
  },

  view() {
    return m('.FiltersPage',
      m('h1.PageHeading', 'Filters'),
      m(TextBox, {
        description: 'New filter:',
        onchange: e => this.newFilter(e.target.value),
      }),
      m(Button, {
        value: 'Add',
        onclick: () => {
          this.filters().push(this.newFilter());
          this.newFilter('');
          this.saveFilters();
        },
      }),
      m(Select, {
        description: 'Existing filters:',
        options: this.filters(),
        onchange: e => this.selectedFilterIndex(e.target.selectedIndex),
      }),
      m(Button, {
        value: 'Remove',
        onclick: () => {
          if (this.filters().length > 0) {
            this.filters().splice(this.selectedFilterIndex(), 1);
            this.saveFilters();
          }
        },
      })
    );
  },
};

export default Filter;
