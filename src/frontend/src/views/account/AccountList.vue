<template>
  <v-container
    fluid
    tag="section">
    <v-card>
      <v-card-title>
        {{ $t('accounts') }}
        <v-spacer />
        <v-btn @click="newAccountDialog.open = true">{{ $t('newAccount') }}</v-btn>
      </v-card-title>
      <v-card-actions>
        <v-dialog v-model="newAccountDialog.open" max-width="500px">
          <v-card>
            <v-card-title>
              <span class="headline">{{ $t('newAccount') }}</span>
            </v-card-title>

            <v-card-text>
              <validation-observer ref="observer" v-slot="{ invalid }">
                <form @submit.prevent="save">
                  <MemberPicker
                    v-model="newAccountDialog.model.memberId"
                    :label="$t('accountOwner')"
                    rules="required" />

                  <currency-field
                    v-model="newAccountDialog.model.monthlyAmount"
                    :label="$t('monthlyAmount')"
                    rules="min_value:1000" />

                  <v-btn text color="blue darken-1" @click="close">
                    {{ $t('cancel') }}
                  </v-btn>

                  <v-btn text color="blue darken-1" class="mr-4" type="submit" :disabled="invalid">
                    {{ $t('save') }}
                  </v-btn>
                </form>
              </validation-observer>
            </v-card-text>
          </v-card>
        </v-dialog>
      </v-card-actions>
      <v-data-table
        :headers="headers"
        :items="items"
        :options.sync="options"
        :server-items-length="total"
        :loading="loading">
        <template v-slot:item.member="{ item }">
          {{ item.member.lastName }} - {{ item.member.firstName }}
        </template>

        <template v-slot:item.balance="{ item }">
          <currency-field :disabled="true" v-model="item.balance" />
        </template>

        <template v-slot:item.monthlyAmount="{ item }">
          <currency-field :disabled="true" v-model="item.monthlyAmount" />
        </template>

        <template v-slot:item.overdueMonthlyAmount="{ item }">
          <currency-field :disabled="true" v-model="item.overdueMonthlyAmount" />
        </template>

        <template v-slot:item.actions="{ item }">
          <v-btn @click="$router.push({name:'AccountDetails',params:{accountId:item.id}})">
            {{ $t('details') }}
          </v-btn>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
import AccountService from '../../services/accounts'
import MemberPicker from '../../components/MemberPicker'
import CurrencyField from '../../components/CurrencyField'
import DatePicker from '../../components/DatePicker'
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default {
  name: 'Accounts',
  components: {
    ValidationProvider,
    ValidationObserver,
    MemberPicker,
    CurrencyField,
    DatePicker
  },
  metaInfo () {
    return {
      title: this.$t('accounts')
    }
  },
  data () {
    return {
      total: 0,
      items: [],
      loading: false,
      options: {
        sortBy: ['id'],
        sortAscending: [false]
      },
      headers: [
        { text: this.$t('code'), value: 'id', sortable: false },
        { text: this.$t('accountOwner'), value: 'member', sortable: false },
        { text: this.$t('balance'), value: 'balance', sortable: false },
        { text: this.$t('monthlyAmount'), value: 'monthlyAmount', sortable: false },
        { text: this.$t('unpaidMonthly'), value: 'overdueMonthlyAmount', sortable: false },
        { text: this.$t('actions'), value: 'actions', sortable: false }
      ],
      newAccountDialog: {
        error: undefined,
        open: false,
        model: {
          memberId: undefined,
          monthlyAmount: undefined,
        },
      }
    }
  },
  watch: {
    options: {
      handler () {
        this.loadAccounts()
      },
      deep: true
    }
  },

  mounted () {
    this.loadAccounts()
  },

  methods: {
    async loadAccounts () {
      this.loading = true
      try {
        const { sortBy, sortAscending, page, itemsPerPage } = this.options
        const params = {
          page: page - 1,
          size: itemsPerPage
        }
        if (sortBy.length > 0) {
          params.sort = sortBy.map((field, index) => `${field},${sortAscending[index] ? 'asc' : 'desc'}`)
        }
        if (this.search) {
          params.search = this.search
        }

        const response = await AccountService.loadAccounts(params)
        this.items = response.content
        this.total = response.totalElements
      } finally {
        this.loading = false
      }
    },

    close () {
      this.newAccountDialog.open = false
      this.$refs.observer.reset()
      this.newAccountDialog.model = {
        memberId: undefined,
        monthlyAmount: undefined,
      }
    },

    async save () {
      try {
        this.newAccountDialog.error = undefined
        await AccountService.createAccount(this.newAccountDialog.model)
        await this.loadAccounts()
        this.close()
      } catch (e) {
        this.newAccountDialog.error = e.message;
      }
    }
  }
}
</script>

<style scoped>

</style>
