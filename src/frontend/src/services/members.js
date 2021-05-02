import qs from 'qs'
import axios from './common-http'

export default {
  loadMembers ({ search, page, size, sort }) {
    return axios.get('/api/v1/member', {
      params: { search, page, size, sort },
      paramsSerializer: params => {
        return qs.stringify(params, { indices: false })
      }
    })
  },

  getMember (id) {
    return axios.get(`/api/v1/member/${id}`)
  },

  deleteMember (id) {
    return axios.delete(`/api/v1/member/${id}`)
  },

  createMember (member) {
    return axios.post('/api/v1/member', member)
  },

  updateMember (id, member) {
    return axios.put(`/api/v1/member/${id}`, member)
  }
}
