import m from 'mithril';
import prop from 'mithril/stream';
import MenuItem from './menuitem';

const Menu = {
  oninit(vn) {
    this.items = vn.attrs.items;
  },

  view() {
    return m('.Menu', [
      m('h3', 'Trace'),
      m('ul.MenuItemList', [
        this.items.map(item => {
          return m(MenuItem, { text: item.text });
        }),
      ]),
    ]);
  },
};

export default Menu;
