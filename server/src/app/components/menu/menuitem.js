import m from 'mithril';

const MenuItem = {
  oninit(vn) {
    this.text = vn.attrs.text;
    this.route = vn.attrs.route;
  },

  view() {
    return m('li',
      m('a.MenuItemLink', { href: this.route, oncreate: m.route.link }, this.text)
    );
  },
};

export default MenuItem;
