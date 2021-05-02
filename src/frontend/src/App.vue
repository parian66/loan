<template>
  <v-fade-transition mode="out-in">
    <router-view />
  </v-fade-transition>
</template>

<script>
import 'vazir-font/dist/Farsi-Digits/font-face-FD.css'
import '@/styles/overrides.sass'

export default {
  name: 'App',
  metaInfo () {
    return {
      title: this.$t('home'),
      titleTemplate: `%s | ${this.$t('app')}`,
      htmlAttrs: { lang: this.$i18n.locale, dir: this.$t('dir') },
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' }
      ]
    }
  }
}
</script>
