<template>
  <v-list>
    <v-list-item
        v-for="([icon, text, path], i) in items"
        :key="i"
        :to="{path}"
        link>
      <v-list-item-icon>
        <v-icon>{{ icon }}</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>{{ text }}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script>
export default {
  name: 'Drawer',
  props: ['drawer'],
  data () {
    return {
      items: [
        ['mdi-cash', this.$t('accounts'), '/accounts'],
        ['mdi-bank', this.$t('loans'), '/loans'],
        ['mdi-account-group', this.$t('members'), '/members'],
        ['mdi-tune-vertical', this.$t('management'), '/management'],
        ['mdi-logout', this.$t('logout'), '/login']
      ]
    }
  }
}
</script>
