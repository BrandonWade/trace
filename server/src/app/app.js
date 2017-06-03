import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter, Switch, Route } from 'react-router-dom';
import Menu from './components/menu/menu';
import Home from './components/containers/home/home';
import Settings from './components/containers/settings/settings';
import Filters from './components/containers/filters/filters';
import './app.css';

const menuItems = [
  {
    text: 'Home',
    path: '/',
    component: Home,
  },
  {
    text: 'Settings',
    path: '/settings',
    component: Settings,
  },
  {
    text: 'Filters',
    path: '/filters',
    component: Filters,
  },
];

const App = () => (
  <HashRouter>
    <div id={ 'container' }>
      <Menu title={ 'Trace' } items={ menuItems } />
      <Switch>
        {
          menuItems.map((item, index) => {
            return <Route exact key={ index } path={ item.path } component={ item.component } />;
          })
        }
      </Switch>
    </div>
  </HashRouter>
);

const root = document.getElementById('root');
ReactDOM.render(
  <App />,
  root
);
