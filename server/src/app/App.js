import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Menu from './components/menu/Menu';
import MenuItems from './data/MenuItems';
import './styles/main.css';

const App = () => (
    <BrowserRouter>
        <div className='container'>
            <Menu title='Trace' />
            <div className='Page'>
                <Switch>
                    {
                        MenuItems.map((item) => {
                            return (
                                <Route
                                    exact={true}
                                    key={item.text}
                                    path={item.path}
                                    component={item.component}
                                />
                            );
                        })
                    }
                </Switch>
            </div>
        </div>
    </BrowserRouter>
);

export default App;
