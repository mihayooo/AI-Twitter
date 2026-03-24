import request from '@/utils/request'

export function getPendingPosts(params) {
  return request({
    url: '/admin/posts/pending',
    method: 'get',
    params
  })
}

export function reviewPost(postId, data) {
  return request({
    url: `/admin/posts/${postId}/review`,
    method: 'put',
    data
  })
}