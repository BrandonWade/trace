import m from 'mithril';
import prop from 'mithril/stream';
import MenuItem from './menuitem';

const Menu = {
  oninit(vn) {
    this.title = vn.attrs.title || '';
    this.items = vn.attrs.items || [];
  },

  view() {
    return m('.Menu', [
      m('h1.MenuHeading', this.title),
      m('ul.MenuItemList', [
        this.items.map(item => {
          return m(MenuItem, { text: item.text, route: item.route });
        }),
      ]),
    ]);
  },
};

export default Menu;
