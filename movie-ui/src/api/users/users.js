import request from '@/utils/request'

// 查询用户表列表
export function listUsers(query) {
  return request({
    url: '/users/users/list',
    method: 'get',
    params: query
  })
}

// 查询用户表详细
export function getUsers(id) {
  return request({
    url: '/users/users/' + id,
    method: 'get'
  })
}

// 新增用户表
export function addUsers(data) {
  return request({
    url: '/users/users',
    method: 'post',
    data: data
  })
}

// 修改用户表
export function updateUsers(data) {
  return request({
    url: '/users/users',
    method: 'put',
    data: data
  })
}

// 删除用户表
export function delUsers(id) {
  return request({
    url: '/users/users/' + id,
    method: 'delete'
  })
}
