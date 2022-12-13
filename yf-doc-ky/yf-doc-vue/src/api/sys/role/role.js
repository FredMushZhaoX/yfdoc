import { post } from '@/utils/request'

export function fetchList() {
  return post('/api/sys/role/list', {})
}

export function listMenu(id) {
  return post('/api/sys/role/list-menus', { id: id })
}

export function saveMenu(data) {
  return post('/api/sys/role/save-menus', data)
}

export function saveData(data) {
  return post('/api/sys/role/save', data)
}

export function apiDetail(id) {
  return post('/api/sys/role/detail', { id: id })
}

