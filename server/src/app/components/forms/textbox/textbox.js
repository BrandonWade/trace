import React, { Component } from 'react';
import './textbox.css';

class TextBox extends Component {
  constructor(props) {
    super(props);

    this.state = {
      description: props.description,
      value: props.value,
      handleChange: props.handleChange,
    };
  }

  render() {
    return (
      <div className={ 'TextBox-Container' }>
        <label className={ 'TextBox-Label' }>{ this.state.description }</label>
        <input type='text' className={ 'TextBox' } value={ this.state.value } onChange={ this.state.handleChange } />
      </div>
    );
  }
};

export default TextBox;
