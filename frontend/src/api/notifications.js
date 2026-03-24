import request from '@/utils/request'

export function getNotifications(params) {
  return request({
    url: '/notifications',
    method: 'get',
    params
  })
}

export function getUnreadNotifications(params) {
  return request({
    url: '/notifications/unread',
    method: 'get',
    params
  })
}

export function getUnreadCount() {
  return request({
    url: '/notifications/unread/count',
    method: 'get'
  })
}

export function markAsRead(notificationId) {
  return request({
    url: `/notifications/${notificationId}/read`,
    method: 'put'
  })
}

export function markAllAsRead() {
  return request({
    url: '/notifications/read/all',
    method: 'put'
  })
}

export function deleteNotification(notificationId) {
  return request({
    url: `/notifications/${notificationId}`,
    method: 'delete'
  })
}