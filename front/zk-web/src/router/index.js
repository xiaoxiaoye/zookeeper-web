import VueRouter from "vue-router";

import Home from '@/components/Home'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        hidden: true
    }
]

const router = new VueRouter({
    mode: 'hash',
    routes
})

export default router