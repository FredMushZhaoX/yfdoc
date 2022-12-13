import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/display.css'
import './styles/element-variables.scss'
import '@/styles/index.scss'

import App from './App'
import store from './store'
import router from './router'

import './icons'
import './permission'
import './utils/error-log'

// 窗口切换检测
import visibility from 'vue-visibility-change'
Vue.use(visibility)

import * as filters from './filters' // global filters

// 引入公共方法
import common from './utils/common'
Vue.use(common)

// 引入vue-UUID组件
import UUID from 'vue-uuid'
Vue.use(UUID)

// UI默认尺寸
Vue.use(Element, {
  size: Cookies.get('size') || 'medium'
})

// 全局过滤器
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
