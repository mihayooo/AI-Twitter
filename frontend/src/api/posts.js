import request from '@/utils/request'

export function getPosts(params) {
  return request({
    url: '/posts',
    method: 'get',
    params
  })
}

export function getTimeline(params) {
  return request({
    url: '/posts/timeline',
    method: 'get',
    params
  })
}

export function getUserPosts(userId, params) {
  return request({
    url: `/posts/user/${userId}`,
    method: 'get',
    params
  })
}

export function getPost(id) {
  return request({
    url: `/posts/${id}`,
    method: 'get'
  })
}

export function createPost(data) {
  return request({
    url: '/posts',
    method: 'post',
    data
  })
}

export function updatePost(id, data) {
  return request({
    url: `/posts/${id}`,
    method: 'put',
    data
  })
}

export function deletePost(id) {
  return request({
    url: `/posts/${id}`,
    method: 'delete'
  })
}

export function likePost(id) {
  return request({
    url: `/posts/${id}/like`,
    method: 'post'
  })
}

export function bookmarkPost(id) {
  return request({
    url: `/posts/${id}/bookmark`,
    method: 'post'
  })
}

export function getHotTags(limit = 20) {
  return request({
    url: '/posts/tags/hot',
    method: 'get',
    params: { limit }
  })
}

// Comment APIs
export function getComments(postId, params) {
  return request({
    url: `/comments/post/${postId}`,
    method: 'get',
    params
  })
}

export function getReplies(commentId, params) {
  return request({
    url: `/comments/${commentId}/replies`,
    method: 'get',
    params
  })
}

export function createComment(data) {
  return request({
    url: '/comments',
    method: 'post',
    data
  })
}

export function deleteComment(commentId) {
  return request({
    url: `/comments/${commentId}`,
    method: 'delete'
  })
}