import request from '@/utils/request'

// 获取用户收藏列表
export function getCollections(id) {
  return request({
    url: '/collections/collections/get',
    method: 'get',
    params: { id }
  })
}

// 添加收藏
export function addCollections(user_id, movie_id) {
  return request({
    url: '/collections/collections/add',
    method: 'get',
    params: { user_id, movie_id }
  })
}

// 删除收藏
export function deleteCollections(user_id, movie_id) {
  return request({
    url: '/collections/collections/delete',
    method: 'get',
    params: { user_id, movie_id }
  })
}
