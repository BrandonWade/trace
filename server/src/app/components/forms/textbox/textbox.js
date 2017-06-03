import React, { Component } from 'react';
import './textbox.css';

class TextBox extends Component {
  constructor(props) {
    super(props);

    this.state = {
      value: props.value,
    };
  }

  render() {
    return (
      <div className={ 'TextBox-Container' }>
        <label className={ 'TextBox-Label' }>{ this.props.description }</label>
        <input type='text' className={ 'TextBox' } value={ this.state.value } onChange={ this.props.handleChange } />
      </div>
    );
  }
};

export default TextBox;
