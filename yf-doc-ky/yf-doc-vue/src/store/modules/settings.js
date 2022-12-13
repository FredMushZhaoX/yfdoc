import defaultSettings from '@/settings'
import { fetchDetail } from '@/api/sys/config/base'

const { tagsView, fixedHeader, sidebarLogo } = defaultSettings

const state = {
  tagsView: tagsView,
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
  siteData: {}
}

const mutations = {

  SET_SITE_DATA: (state, siteData) => {
    state.siteData = siteData
  }
}

const actions = {
  // 获取网站配置信息
  getSite({ commit }) {
    return new Promise((resolve, reject) => {
      // 重新获取
      fetchDetail({}).then(res => {
        const { data } = res
        commit('SET_SITE_DATA', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

