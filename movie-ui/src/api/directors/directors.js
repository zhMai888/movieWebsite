import request from '@/utils/request'

// 查询导演列表
export function listDirectors(query) {
  return request({
    url: '/directors/directors/list',
    method: 'get',
    params: query
  })
}

// 查询导演详细
export function getDirectors(id) {
  return request({
    url: '/directors/directors/' + id,
    method: 'get'
  })
}

// 新增导演
export function addDirectors(data) {
  return request({
    url: '/directors/directors',
    method: 'post',
    data: data
  })
}

// 修改导演
export function updateDirectors(data) {
  return request({
    url: '/directors/directors',
    method: 'put',
    data: data
  })
}

// 删除导演
export function delDirectors(id) {
  return request({
    url: '/directors/directors/' + id,
    method: 'delete'
  })
}
