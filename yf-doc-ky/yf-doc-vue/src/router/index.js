import Vue from 'vue'
import Router from 'vue-router'
import LoginLayout from '@/views/login/layout/LoginLayout'

Vue.use(Router)
import Layout from '@/layout'

// 静态通用路由
export const constantRoutes = [
  {
    path: '/login',
    component: LoginLayout,
    hidden: true,
    children: [
      {
        path: '',
        name: 'Login',
        component: () => import('@/views/login/index'),
        hidden: true,
        meta: {
          title: '用户登录'
        }
      }, {
        path: '/register',
        name: 'Register',
        layout: LoginLayout,
        component: () => import('@/views/login/register'),
        hidden: true
      }
    ]
  },

  {
    path: '/sync/:token/:roleType',
    name: 'LoginSync',
    component: () => import('@/views/login/sync'),
    hidden: true,
    meta: { title: '登录成功', noCache: true }
  },

  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },

  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: '个人资料', icon: 'user', noCache: true }
      }
    ]
  }

]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
