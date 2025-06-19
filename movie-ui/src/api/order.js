import request from '@/utils/request'

export function createAlipayOrder(outTradeNo, totalAmount = 8.88, subject = '购买VIP') {
  return request({
    url: `/alipay/pay?outTradeNo=${outTradeNo}&totalAmount=${totalAmount}&subject=${subject}`,
    method: 'get'
  })
}
