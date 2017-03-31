import m from 'mithril';

const TextBox = {
  oninit(vn) {
    this.description = vn.attrs.description;
    this.value = vn.attrs.value;
    this.onchange = vn.attrs.onchange;
  },

  view() {
    return m('.TextBoxContainer', [
      m('label.TextBoxLabel', m.trust(this.description)),
      m('input[type=text].TextBox', {
        value: this.value,
        onchange: this.onchange,
      }),
    ]);
  },
};

export default TextBox;
