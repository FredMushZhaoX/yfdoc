import { constantRoutes } from '@/router'
import { fetchRoutes } from '@/api/sys/menu/menu'
import Layout from '@/layout'
import WebLayout from '@/layout/WebLayout'

/**
 * 懒加载路由
 * @param path
 * @returns {function(*=): any}
 */
export const loadView = (path) => {
  return (resolve) => require([`@/views/${path}`], resolve)
}

/**
 * 将后台返回的路由表组装成前端需要的格式
 * @param {*} routes
 */
export function getAsyncRoutes(routes) {
  const res = []
  const keys = ['path', 'name', 'children', 'redirect', 'alwaysShow', 'meta', 'hidden']
  if (!routes || routes.length == 0) {
    return res
  }
  routes.forEach(item => {
    const newItem = {}
    if (item.component) {
      if (item.component === 'Layout') {
        newItem.component = Layout
      } else if (item.component === 'WebLayout') {
        newItem.component = WebLayout
      } else {
        newItem.component = loadView(item.component)
      }
    }
    for (const key in item) {
      if (keys.includes(key)) {
        newItem[key] = item[key]
      }
    }
    if (newItem.children && newItem.children.length) {
      newItem.children = getAsyncRoutes(item.children)
    }
    res.push(newItem)
  })
  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {

  generateRoutes({ commit }) {
    return new Promise(async resolve => {
      // 获取到后台路由
      const routes = await fetchRoutes()
      // 对路由格式进行处理
      const asyncRoutes = getAsyncRoutes(routes.data)
      commit('SET_ROUTES', asyncRoutes)
      resolve(asyncRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
