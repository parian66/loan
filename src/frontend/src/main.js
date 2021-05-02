import Vue from 'vue'

import App from './App.vue'
import Meta from 'vue-meta'
import vuetify from './plugins/vuetify'
import router from './router'
import i18n from './i18n'

Vue.use(Meta)

new Vue({
  router,
  i18n,
  vuetify,
  render: h => h(App)
}).$mount('#app')
