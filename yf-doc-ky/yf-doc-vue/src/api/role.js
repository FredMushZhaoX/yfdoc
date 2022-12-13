import { post } from '@/utils/request'

export function getRoutes() {
  return post('/api/routes', {})
}

export function getRoles() {
  return post('/api/roles', {})
}

export function addRole(data) {
  return post('/api/role', data)
}

export function updateRole(id, data) {
  return post(`/role/${id}`, data)
}

export function deleteRole(id) {
  return post(`/role/${id}`, {})
}
