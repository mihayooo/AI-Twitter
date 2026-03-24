import request from '@/utils/request'

export function getUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'get'
  })
}

export function updateProfile(data) {
  return request({
    url: '/users/profile',
    method: 'put',
    data
  })
}

export function followUser(id) {
  return request({
    url: `/users/${id}/follow`,
    method: 'post'
  })
}

export function getFollowers(id, params) {
  return request({
    url: `/users/${id}/followers`,
    method: 'get',
    params
  })
}

export function getFollowing(id, params) {
  return request({
    url: `/users/${id}/following`,
    method: 'get',
    params
  })
}