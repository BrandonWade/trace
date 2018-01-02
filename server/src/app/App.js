import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { HashRouter, Switch, Route } from 'react-router-dom';
import Menu from './components/menu/Menu';
import MenuItems from './data/MenuItems';
import './styles/main.css';

class App extends Component {
    render() {
        return (
            <HashRouter>
                <div className={'container'}>
                    <Menu title={'Trace'} />
                    <div className={'Page'}>
                        <Switch>
                            {
                                MenuItems.map((item) => <Route exact key={item.text} path={item.path} component={item.component} />)
                            }
                        </Switch>
                    </div>
                </div>
            </HashRouter>
        );
    }
}

export default App;
