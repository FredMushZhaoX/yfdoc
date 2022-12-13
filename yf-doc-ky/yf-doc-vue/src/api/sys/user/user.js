import { post, download } from '@/utils/request'

export function updateData(data) {
  return post('/api/sys/user/update', data)
}

export function saveData(data) {
  return post('/api/sys/user/save', data)
}

export function userReg(data) {
  return post('/api/sys/user/reg', data)
}

export function updatePass(data) {
  return post('/api/sys/user/update-pass', data)
}

export function findInfo(token) {
  return post('/api/sys/user/info', { token: token })
}

export function fetchDetail(id) {
  return post('/api/sys/user/detail', { id: id })
}

/**
 * 搜索用户列表
 * @param data
 * @returns {*}
 */
export function fetchPaging(data) {
  return post('/api/sys/user/paging', data)
}

/**
 * 导出
 * @param data
 */
export function exportExcel(data) {
  return download('/api/sys/user/export', data, '导出的用户.xlsx')
}

/**
 * 批量修改部门
 * @param data
 * @returns {Promise}
 */
export function apiBatchDept(data) {
  return post('/api/sys/user/batch-dept', data)
}


/**
 * 批量修改角色
 * @param data
 * @returns {Promise}
 */
export function apiBatchRole(data) {
  return post('/api/sys/user/batch-role', data)
}
