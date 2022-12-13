export default {
  install(Vue, options) {
    Vue.prototype.$navBack = function() {
      window.history.back()
    }
  }
}
