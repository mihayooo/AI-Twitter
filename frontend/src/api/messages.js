import request from '@/utils/request'

export function getMessages(otherUserId, params) {
  return request({
    url: `/messages/conversation/${otherUserId}`,
    method: 'get',
    params
  })
}

export function getUserList() {
  return request({
    url: '/messages/users',
    method: 'get'
  })
}

export function sendMessage(receiverId, content) {
  return request({
    url: '/messages',
    method: 'post',
    params: { receiverId },
    data: { content }
  })
}

export function markAsRead(senderId) {
  return request({
    url: `/messages/read/${senderId}`,
    method: 'put'
  })
}

export function markAllAsRead(senderId) {
  return request({
    url: `/messages/read/all/${senderId}`,
    method: 'put'
  })
}