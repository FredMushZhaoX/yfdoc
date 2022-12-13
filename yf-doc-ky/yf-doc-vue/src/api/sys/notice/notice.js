import { post } from '@/utils/request'

export function saveData(data) {
  return post('/api/sys/notice/save', data)
}

export function fetchDetail(id) {
  return post('/api/sys/notice/detail', { id: id })
}
