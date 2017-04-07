import m from 'mithril';
import './textbox.css';

const TextBox = {
  oninit(vn) {
    this.description = vn.attrs.description;
    this.value = vn.attrs.value;
    this.onchange = vn.attrs.onchange;
  },

  view() {
    return m('.TextBox-Container', [
      m('label.TextBox-Label', m.trust(this.description)),
      m('input[type=text].TextBox', {
        value: this.value,
        onchange: this.onchange,
      }),
    ]);
  },
};

export default TextBox;
