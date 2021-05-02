import axios from './common-http'

export default {
  getToken () {
    try {
      const user = JSON.parse(localStorage.getItem('user'))
      return user && user.token
    } catch (e) {
      return undefined
    }
  },

  isLoggedIn () {
    return Boolean(this.getToken())
  },

  async login ({ username, password }) {
    const user = await axios.post('/api/auth/login', { username, password })
    localStorage.setItem('user', JSON.stringify(user))
  },

  logout () {
    localStorage.removeItem('user')
  }
}
