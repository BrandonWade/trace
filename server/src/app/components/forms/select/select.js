import m from 'mithril';

const Select = {
  oninit(vn) {
    this.description = vn.attrs.description;
    this.options = vn.attrs.options || [];
    this.size = Math.max(this.options.length, 10);
    this.onchange = vn.attrs.onchange;
  },

  view() {
    return m('.SelectContainer', [
      m('label.SelectLabel', m.trust(this.description)),
      m('select.Select', {
        size: this.size,
        multiple: this.multiple,
        onchange: this.onchange,
      }, [
        this.options.map(option => m('option', option))
      ]),
    ]);
  },
};

export default Select;
