import qs from 'qs'
import axios from './common-http'

export default {
  loadAccounts ({ status, page, size, sort }) {
    return axios.get('/api/v1/account', {
      params: { status, page, size, sort },
      paramsSerializer: params => {
        return qs.stringify(params, { indices: false })
      }
    })
  },

  getAccount (id) {
    return axios.get(`/api/v1/account/${id}`)
  },

  createAccount ({ monthlyAmount, memberId }) {
    return axios.post('/api/v1/account', { monthlyAmount, memberId })
  },

  loadTransactions ({ accountId, status, page, size, sort }) {
    return axios.get(`/api/v1/account/${accountId}/transaction`, {
      params: { status, page, size, sort },
      paramsSerializer: params => {
        return qs.stringify(params, { indices: false })
      }
    })
  },

  saveTransactions ({ accountId, amount, type, description }) {
    return axios.post(`/api/v1/account/${accountId}/transaction`,
      { amount, type, description }
    )
  },

  loadMonthlies ({ accountId, status, page, size, sort }) {
    return axios.get(`/api/v1/account/${accountId}/monthly`, {
      params: { status, page, size, sort },
      paramsSerializer: params => {
        return qs.stringify(params, { indices: false })
      }
    })
  },

  payMonthly ({ accountId, monthlyId, createTransaction, description }) {
    return axios.put(`/api/v1/account/${accountId}/monthly/${monthlyId}/pay`,
      { createTransaction, description }
    )
  },
}
