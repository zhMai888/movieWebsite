import request from '@/utils/request'

// 查询电影表列表
export function listMovies(query) {
  return request({
    url: '/movies/movies/list',
    method: 'get',
    params: query
  })
}

// 查询电影表详细
export function getMovies(id) {
  return request({
    url: '/movies/movies/' + id,
    method: 'get'
  })
}

// 新增电影表
export function addMovies(data) {
  return request({
    url: '/movies/movies',
    method: 'post',
    data: data
  })
}

// 修改电影表
export function updateMovies(data) {
  return request({
    url: '/movies/movies',
    method: 'put',
    data: data
  })
}

// 删除电影表
export function delMovies(id) {
  return request({
    url: '/movies/movies/' + id,
    method: 'delete'
  })
}

// 搜索电影地区
export function searchAreas(query) {
  return request({
    url: '/movies/movies/searchAreas',
    method: 'get',
    params: query
  })
}

// 搜索电影类型
export function searchGenres(query) {
  return request({
    url: '/movies/movies/searchGenres',
    method: 'get',
    params: query
  })
}

// 演员列表
export function listActors(query) {
  return request({
    url: '/movies/movies/listActors',
    method: 'get',
    params: query
  })
}

// 导演列表
export function listDirectors(query) {
  return request({
    url: '/movies/movies/listDirectors',
    method: 'get',
    params: query
  })
}

