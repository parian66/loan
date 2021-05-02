import Vue from 'vue'
import Vuetify from 'vuetify/lib/framework'
import i18n from '../i18n'
import './vuetify-money.js';
import './vee-validate';


Vue.use(Vuetify)

export default new Vuetify({
  rtl: i18n.t('dir') === 'rtl',
  lang: {
    t: (key, ...params) => i18n.t(key, params),
  },
})
