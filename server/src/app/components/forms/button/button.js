import React, { Component } from 'react';

class Button extends Component {
  constructor(props) {
    super(props);

    this.state = {
      value: props.value,
      handleClick: props.handleClick,
    };
  }

  render() {
    return (
      <button onClick={ this.state.handleClick }>{ this.state.value }</button>
    );
  }
};

export default Button;
