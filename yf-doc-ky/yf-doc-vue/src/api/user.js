import { post } from '@/utils/request'

export function login(data) {
  return post('/api/sys/user/login', data)
}

export function getInfo(token) {
  return post('/api/sys/user/info', { token: token })
}

export function logout() {
  return post('/api/sys/user/logout', {})
}

export function reg(data) {
  return post('/api/sys/user/reg', data)
}

export function mobileLogin(data) {
  return post('/api/sys/user/mobile-login', data)
}

export function resetPass(data) {
  return post('/api/sys/user/reset-pass', data)
}
