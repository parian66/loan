import qs from 'qs'
import axios from './common-http'

export default {
  loadLoans ({ memberId, status, page, size, sort }) {
    return axios.get('/api/v1/loan', {
      params: { memberId, status, page, size, sort },
      paramsSerializer: params => {
        return qs.stringify(params, { indices: false })
      }
    })
  },

  getLoan (id) {
    return axios.get(`/api/v1/loan/${id}`)
  },

  createLoan ({ rate, amount, memberId, installmentCount, firstInstallmentDate, description }) {
    return axios.post('/api/v1/loan', { rate, amount, memberId, installmentCount, firstInstallmentDate, description })
  },

  repay ({ id, count }) {
    return axios.put(`/api/v1/loan/${id}/repay`, { count })
  },
}
