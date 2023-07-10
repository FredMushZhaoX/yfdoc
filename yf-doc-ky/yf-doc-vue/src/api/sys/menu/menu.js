import { post } from '@/utils/request'
export function fetchRoutes() {
  return post('/api/sys/menu/routes')
}

export function fetchTree() {
  return post('/api/sys/menu/tree')
}

export function fetchDetail(id) {
  return post('/api/sys/menu/detail', { id: id })
}

export function deleteData(ids) {
  const data = { ids: ids }
  return post('/api/sys/menu/delete', data)
}

export function saveData(data) {
  return post('/api/sys/menu/save', data)
}

export function sortData(id, sort) {
  const data = { id: id, sort: sort }
  return post('/api/sys/menu/sort', data)
}
