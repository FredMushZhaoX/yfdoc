import { post } from '@/utils/request'

export function fetchTree(data) {
  return post('/api/sys/dic/value/tree', data)
}

export function saveValue(data) {
  return post('/api/sys/dic/value/save', data)
}

export function deleteValue(data) {
  return post('/api/sys/dic/value/delete', data)
}
