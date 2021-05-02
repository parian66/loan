import i18n from '../i18n'
import axios from 'axios'
import router from '../router'
import authorization from './authorization'

function addAcceptLanguageHeader (config) {
  config.headers['Accept-Language'] = i18n.locale
}

function addAuthorizationHeader (config) {
  const token = authorization.getToken()

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
}

// Add a request interceptor
axios.interceptors.request.use(config => {
  // Do something before request is sent
  addAcceptLanguageHeader(config)
  addAuthorizationHeader(config)

  return config
}, function (error) {
  // Do something with request error
  return Promise.reject(error)
})

// Add a response interceptor
axios.interceptors.response.use(response => {
  // Any status code that lie within the range of 2xx cause this function to trigger
  return response.data ? response.data : response
}, function (error) {
  if (error.response.status === 401) {
    router.push('/login')
  }
  // Any status codes that falls outside the range of 2xx cause this function to trigger
  // Do something with response error
  return Promise.reject(error)
})

export default axios
