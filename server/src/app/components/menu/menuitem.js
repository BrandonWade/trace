import m from 'mithril';

const MenuItem = {
  oninit(vn) {
    this.text = vn.attrs.text;
  },

  view() {
    return m('li.MenuItem', this.text);
  },
};

export default MenuItem;
