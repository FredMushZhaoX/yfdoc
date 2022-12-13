import Cookies from 'js-cookie'
import { constantRoutes } from '@/router'

const TokenKey = 'token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

/**
 * 校验访问权限
 * @param routes
 * @param path
 * @returns {boolean}
 */
export function ifAccess(routes, path) {
  // 校验静态路由
  const access = checkAccess(constantRoutes, path)

  if (access === true) {
    return true
  }

  // 校验动态路由
  return checkAccess(routes, path)
}

/**
 * 校验是否有权限通行
 * @param routes
 * @param path
 * @returns {boolean}
 */
function checkAccess(routes, path) {
  // 首页权限都可放行
  if (path === '/' || path === 'index') {
    return true
  }

  if (routes == null || routes.length === 0) {
    return false
  }

  // 静态路由校验
  for (let i = 0; i < routes.length; i++) {
    // 截断参数定义
    let route = routes[i].path
    const index = route.indexOf(':')
    if (index !== -1) {
      route = route.substring(0, index)
    }

    // 不匹配根目录
    if (path.startsWith(route) && route !== '/') {
      return true
    }

    // 匹配子目录
    const children = routes[i].children
    if (children !== undefined) {
      const result = checkAccess(children, path)
      if (result) {
        return true
      }
    }
  }

  return false
}
