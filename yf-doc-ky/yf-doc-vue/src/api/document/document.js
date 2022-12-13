import { post } from '@/utils/request'

/**
 * 资料详情
 * @param id
 * @returns {*}
 */
export function fetchDetail(id) {
  return post('/api/doc/doc/detail', { id: id })
}

/**
 * 资料分页
 * @param id
 * @returns {*}
 */
export function fetchPaging(data) {
  return post('/api/doc/doc/paging', data)
}

/**
 * 保存资料
 * @param data
 */
export function saveData(data) {
  return post('/api/doc/doc/save', data)
}

/**
 * 删除资料
 * @param data
 */
export function deleteById(id) {
  return post('/api/doc/doc/delete', { ids: [id] })
}
/**
 * 提交审核
 * @param data
 */
export function submiReview(id) {
  return post('/api/doc/doc/submiReview', { id: id })
}

/**
 * 审核通过
 * @param data
 */
export function approved(id) {
  return post('/api/doc/doc/approved', { id: id })
}

/**
 * 审核不通过通过
 * @param data
 */
export function approvedFailed(data) {
  return post('/api/doc/doc/approvedFailed', data)
}
