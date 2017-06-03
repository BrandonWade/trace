import React, { Component } from 'react';
import './select.css';

class Select extends Component {
  constructor(props) {
    super(props);

    this.state = {
      description: props.description,
      options: props.options || [],
      size: Math.max(props.options.length, 10),
      handleChange: props.handleChange,
    };
  }

  render() {
    return (
      <div className={ 'Select-Container' }>
        <label className={ 'Select-Label' }>{ this.state.description }</label>
        <select className={ 'Select' } size={ this.state.size } multiple={ this.state.multiple } onChange={ this.state.handleChange }>
          {
            this.state.options.map((option, index) => {
              return <option key={ index }>{ option }</option>;
            })
          }
        </select>
      </div>
    );
  }
};

export default Select;
