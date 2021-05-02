<template>
  <v-app>
    <v-navigation-drawer :right="$t('dir') === 'rtl'" v-model="drawer" app>
      <Drawer />
    </v-navigation-drawer>
    <v-app-bar app>
      <v-app-bar-nav-icon @click="drawer = !drawer" />
    </v-app-bar>
    <v-main>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Drawer from '../components/Drawer'

export default {
  name: 'Home',
  components: {
    Drawer
  },
  data () {
    return {
      drawer: false
    }
  }
}
</script>

<style scoped>

</style>
