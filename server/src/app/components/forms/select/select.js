import React, { Component } from 'react';
import './select.css';

class Select extends Component {
  constructor(props) {
    super(props);

    this.state = {
      size: Math.max(this.props.options.length, 20),
    };
  }

  render() {
    return (
      <div className={ 'Select-Container' }>
        <label className={ 'Select-Label' }>{ this.props.description }</label>
        <select className={ 'Select' } size={ this.state.size } multiple={ this.props.multiple } onChange={ this.props.handleChange }>
          {
            this.props.options.map((option, index) => {
              return <option key={ index }>{ option }</option>;
            })
          }
        </select>
      </div>
    );
  }
};

export default Select;
