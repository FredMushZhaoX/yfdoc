import { post } from '@/utils/request'
export function pagingTree(data) {
  return post('/api/sys/depart/paging', data)
}

export function fetchTree(data) {
  return post('/api/sys/depart/tree', data)
}

export function fetchTreeSelect(data) {
  return post('/api/sys/depart/tree-select', data)
}

export function fetchDetail(id) {
  const data = { id: id }
  return post('/api/sys/depart/detail', data)
}

export function deleteData(ids) {
  const data = { ids: ids }
  return post('/api/sys/depart/delete', data)
}

export function saveData(data) {
  return post('/api/sys/depart/save', data)
}

export function sortData(id, sort) {
  const data = { id: id, sort: sort }
  return post('/api/sys/depart/sort', data)
}
