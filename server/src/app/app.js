import m from 'mithril';
import Menu from './components/menu/menu';
import Home from './components/pages/home/home';
import Settings from './components/pages/settings/settings';
import Filters from './components/pages/filters/filters';
import './app.css';

const body = document.body;
const menuItems = [
  {
    text: 'Home',
    route: '/',
  },
  {
    text: 'Settings',
    route: '/settings',
  },
  {
    text: 'Filters',
    route: '/filters',
  },
];

m.render(body, [
  m(Menu, { title: 'Trace', items: menuItems }),
  m('.Page', { id: 'PageContainer' }),
]);

const page = document.getElementById('PageContainer');
m.route(page, '/', {
  '/': Home,
  '/settings': Settings,
  '/filters': Filters,
});
