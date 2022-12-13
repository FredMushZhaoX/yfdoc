import { post } from '@/utils/request'

export function fetchDetail() {
  return post('/api/sys/config/live/detail', { id: '1' })
}

export function saveData(data) {
  return post('/api/sys/config/live/save', data)
}
