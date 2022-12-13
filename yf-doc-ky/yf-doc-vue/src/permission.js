import router from './router'
import store from './store'
import NProgress from 'nprogress'
import { Message } from 'element-ui'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false })

// 白名单
const whiteList = ['/login', '/register', '/forgot', '401', '404', '/cert', '/sync']

router.beforeEach(async(to, from, next) => {
  // 进度条
  NProgress.start()

  // 获取网站基本信息
  let siteData = store.getters.siteData
  if (!siteData.siteName) {
    siteData = await store.dispatch('settings/getSite')
  }

  // 页面标题
  document.title = getPageTitle(siteData.siteName, to.meta.title)

  // 判断用户是否登录
  const hasToken = getToken()

  if (hasToken) {
    // 从登陆页面直接跳转到首页
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
      return
    }

    // 判断角色
    const hasRoles = store.getters.roles && store.getters.roles.length > 0
    if (hasRoles) {
      next()
    } else {
      try {
        // 获取用户信息
        await store.dispatch('user/getInfo')
        // 加载动态路由
        const routes = await store.dispatch('permission/generateRoutes')

        // 校验是否有URL的权限
        // const access = ifAccess(routes, to.path)
        // 无权限
        // if (!access) {
        //   next({ path: '/401' })
        //   NProgress.done()
        //   return
        // }

        // 管理员未获取任何菜单权限
        if (routes.length == 0 && store.getters.roleType == 2) {
          Message.error('您无权访问此系统，请联系管理员进行菜单授权！')
          await store.dispatch('user/resetToken')
          next(`/login?redirect=${to.path}`)
          return
        }

        router.addRoutes(routes)
        // 确保路由已完成并不改变路由地址
        next({ ...to, replace: true })
      } catch (error) {
        // 清理会话返登录
        await store.dispatch('user/resetToken')
        Message.error(error || 'Has Error')
        next(`/login?redirect=${to.path}`)
        NProgress.done()
      }
    }
  } else {
    // 检测白名单数据
    let white = false
    for (let i = 0; i < whiteList.length; i++) {
      if (to.path.startsWith(whiteList[i])) {
        white = true
      }
    }

    // 无需授权的页面
    if (white) {
      next()
    } else {
      // 跳转到登录
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
