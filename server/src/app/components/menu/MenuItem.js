import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class MenuItem extends Component {
  constructor(props) {
    super(props);

    this.state = {
      text: props.text,
      path: props.path,
    };
  }

  render() {
    return (
      <li className={ 'MenuItem' }>
        <Link className={ 'MenuItem-link' } to={ this.state.path }>{ this.state.text }</Link>
      </li>
    );
  }
};

export default MenuItem;
