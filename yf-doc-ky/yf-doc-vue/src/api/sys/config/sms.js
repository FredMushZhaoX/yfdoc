import { post } from '@/utils/request'

export function fetchDetail() {
  return post('/api/sys/config/sms/detail', {})
}

export function saveData(data) {
  return post('/api/sys/config/sms/save', data)
}
