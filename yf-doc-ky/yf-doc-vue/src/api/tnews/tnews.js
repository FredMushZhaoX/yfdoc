import { post } from '@/utils/request'

/**
 * 新闻详情
 * @param id
 * @returns {*}
 */
export function fetchDetail(id) {

  return post('/api/news/news/detail', { id: id })
}

/**
 * 资料分页
 * @param id
 * @returns {*}
 */
export function fetchPaging(data) {
  return post('/api/news/news/paging', data)
}

/**
 * 保存资料
 * @param data
 */
export function saveData(data) {
  return post('/api/news/news/save', data)
}

/**
 * 发布新闻
 * @param data
 */
export function fbNews(id) {
  return post('/api/news/news/fbNews', {id:id})
}

/**
 * 取消发布新闻
 * @param data
 */
export function qxFbNews(id) {
  return post('/api/news/news/qxFbNews', {id:id})
}
