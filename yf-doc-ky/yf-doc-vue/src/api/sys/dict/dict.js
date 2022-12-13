import { post } from '@/utils/request'

export function saveDic(data) {
  return post('/api/sys/dic/save', data)
}
