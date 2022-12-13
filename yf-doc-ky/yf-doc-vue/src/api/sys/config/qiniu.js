import { post } from '@/utils/request'

export function fetchToken() {
  return post('/api/common/qiniu/token')
}
