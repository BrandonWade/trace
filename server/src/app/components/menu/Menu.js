import React, { Component } from 'react';
import MenuItem from './MenuItem';
import MenuItems from '../../data/MenuItems';
import './Menu.css';

class Menu extends Component {
    constructor(props) {
        super(props);

        this.state = {
            title: props.title || '',
        };
    }

    render() {
        return (
            <div className='Menu'>
                <h1 className='Menu-heading'>{this.state.title}</h1>
                <ul className='Menu-itemList'>
                    {
                        MenuItems.map((item) => {
                            return (
                                <MenuItem
                                    key={item.text}
                                    text={item.text}
                                    path={item.path}
                                />
                            );
                        })
                    }
                </ul>
            </div>
        );
    }
}

export default Menu;
