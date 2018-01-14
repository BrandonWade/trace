import Home from '../pages/Home';
import Settings from '../pages/Settings';
import Filters from '../pages/Filters';

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

export default menuItems;
