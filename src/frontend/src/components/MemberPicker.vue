<template>
  <v-autocomplete
      v-model="cmpValue"
      :items="items"
      :loading="loading"
      :search-input.sync="search"
      :label="$t('member')"
      :item-text="text"
      item-value="id">
    <template v-slot:item="data">
      <v-list-item-content>
        <v-list-item-title>{{ data.item.firstName }} {{ data.item.lastName }}</v-list-item-title>
        <v-list-item-subtitle>{{ data.item.nationalCode }}</v-list-item-subtitle>
      </v-list-item-content>
    </template>
  </v-autocomplete>
</template>

<script>
import MemberService from '../services/members'

export default {
  name: 'MemberPicker',

  model: { prop: 'value', event: 'input' },

  props: {
    value: {
      type: [Number],
    },
  },

  data () {
    return {
      loading: false,
      search: null,
      items: []
    }
  },

  computed: {
    cmpValue: {
      get: function () {
        return this.value;
      },
      set: function (newValue) {
        this.$emit('input', newValue);
      }
    }
  },

  watch: {
    search () {
      this.searchMember()
    }
  },

  methods: {
    update (newValue) {
      this.$emit('input', newValue)
    },

    text (item) {
      return `${item.firstName} ${item.lastName}`
    },

    async searchMember () {
      // Items have already been loaded
      if (this.items.length > 0) {
        return
      }

      this.loading = true
      try {
        const params = {
          search: this.search,
          sortBy: ['lastName', 'firstName'],
          sortAscending: [true, true]
        }
        const response = await MemberService.loadMembers(params)
        this.items = response.content
      } finally {
        this.loading = false
      }
    }
  },

  async mounted () {
    if (this.value) {
      this.selected = await MemberService.getMember(this.value)
      this.items.push(this.selected)
    }
    await this.searchMember()
  }
}
</script>
