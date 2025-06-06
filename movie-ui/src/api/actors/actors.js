import request from '@/utils/request'

// 查询演员列表
export function listActors(query) {
  return request({
    url: '/actors/actors/list',
    method: 'get',
    params: query
  })
}

// 查询演员详细
export function getActors(id) {
  return request({
    url: '/actors/actors/' + id,
    method: 'get'
  })
}

// 新增演员
export function addActors(data) {
  return request({
    url: '/actors/actors',
    method: 'post',
    data: data
  })
}

// 修改演员
export function updateActors(data) {
  return request({
    url: '/actors/actors',
    method: 'put',
    data: data
  })
}

// 删除演员
export function delActors(id) {
  return request({
    url: '/actors/actors/' + id,
    method: 'delete'
  })
}
