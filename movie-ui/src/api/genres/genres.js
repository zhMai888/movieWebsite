import request from '@/utils/request'

// 查询类别表列表
export function listGenres(query) {
  return request({
    url: '/genres/genres/list',
    method: 'get',
    params: query
  })
}

// 查询类别表详细
export function getGenres(id) {
  return request({
    url: '/genres/genres/' + id,
    method: 'get'
  })
}

// 新增类别表
export function addGenres(data) {
  return request({
    url: '/genres/genres',
    method: 'post',
    data: data
  })
}

// 修改类别表
export function updateGenres(data) {
  return request({
    url: '/genres/genres',
    method: 'put',
    data: data
  })
}

// 删除类别表
export function delGenres(id) {
  return request({
    url: '/genres/genres/' + id,
    method: 'delete'
  })
}
