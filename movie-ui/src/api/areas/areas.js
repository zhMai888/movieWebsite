import request from '@/utils/request'

// 查询地区列表
export function listAreas(query) {
  return request({
    url: '/areas/areas/list',
    method: 'get',
    params: query
  })
}

// 查询地区详细
export function getAreas(id) {
  return request({
    url: '/areas/areas/' + id,
    method: 'get'
  })
}

// 新增地区
export function addAreas(data) {
  return request({
    url: '/areas/areas',
    method: 'post',
    data: data
  })
}

// 修改地区
export function updateAreas(data) {
  return request({
    url: '/areas/areas',
    method: 'put',
    data: data
  })
}

// 删除地区
export function delAreas(id) {
  return request({
    url: '/areas/areas/' + id,
    method: 'delete'
  })
}
