import m from 'mithril';
import Menu from './components/menu/menu';
import './app.css';

const body = document.body;
const items = [
  {
    text: 'Settings',
    route: '',
  },
  {
    text: 'Connection',
    route: '',
  },
  {
    text: 'Filters',
    route: '',
  },
]

m.render(body,
  m(Menu, { items }),
);
