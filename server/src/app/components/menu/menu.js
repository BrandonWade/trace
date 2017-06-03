import React, { Component } from 'react';
import MenuItem from './menuitem';
import './menu.css';

class Menu extends Component {
  constructor(props) {
    super(props);

    this.state = {
      title: props.title || '',
      items: props.items || [],
    };
  }

  render() {
    return (
      <div className={ 'Menu' }>
        <h1 className={ 'Menu-Heading' }>{ this.state.title }</h1>
        <ul className={ 'Menu-ItemList' }>
          {
            this.state.items.map((item, index) => {
              return <MenuItem key={ index } text={ item.text } path={ item.path } />;
            })
          }
        </ul>
      </div>
    );
  }
};

export default Menu;
