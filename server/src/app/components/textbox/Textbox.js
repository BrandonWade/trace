import React, { Component } from 'react';
import './Textbox.css';

class TextBox extends Component {
  constructor(props) {
    super(props);

    this.state = {
      value: props.value,
    };
  }

  render() {
    return (
      <div className={'TextBox-wrapper'}>
        <label className={'TextBox-label'}>{this.props.description}</label>
        <input type={'text'} className={'TextBox'} value={this.state.value} onChange={this.props.handleChange} />
      </div>
    );
  }
};

export default TextBox;
