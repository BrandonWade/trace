import m from 'mithril';

const Button = {
  oninit(vn) {
    this.value = vn.attrs.value;
    this.onclick = vn.attrs.onclick;
  },

  view() {
    return m('Button.Button', { onclick: this.onclick }, this.value);
  },
};

export default Button;
