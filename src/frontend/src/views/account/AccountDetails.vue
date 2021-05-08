<template>
  <v-container
      fluid
      tag="section">
    <v-card>
      <v-card-actions>
        <v-btn @click="$router.push({path:'/accounts'})">
          {{ $t('back') }}
        </v-btn>
        <v-spacer />
        <v-btn @click="transactions.dialog.open = true">
          {{ $t('createTransaction') }}
        </v-btn>
      </v-card-actions>
      <v-card-text v-if="account.member">
        <v-row>
          <v-col cols="12" sm="6" md="3">
            {{ $t('accountOwner') }}: {{ account.member.firstName }} - {{ account.member.lastName }}
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <currency-field :disabled="true" :label="$t('balance')" v-model="account.balance" />
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <currency-field :disabled="true" :label="$t('unpaidMonthly')" v-model="account.overdueMonthlyAmount" />
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <currency-field :disabled="true" :label="$t('monthlyAmount')" v-model="account.monthlyAmount" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-text>
        <v-tabs>
          <v-tab>
            <v-icon>
              mdi-calendar-range
            </v-icon>
            {{ $t('unpaidMonthly') }}
          </v-tab>

          <v-tab>
            <v-icon>
              mdi-credit-card
            </v-icon>
            {{ $t('transactions') }}
          </v-tab>

          <v-tab-item>
            <v-data-table
                :headers="monthlies.headers"
                :items="monthlies.items"
                :options.sync="monthlies.options"
                :server-items-length="monthlies.total"
                :loading="monthlies.loading">

              <template v-slot:item.date="{ item }">
                <date-picker :disabled="true" v-model="item.date" />
              </template>

              <template v-slot:item.amount="{ item }">
                <currency-field :disabled="true" v-model="item.amount" />
              </template>

              <template v-slot:item.status="{ item }">
                <v-chip :color="getColor(item)">
                  {{ $t(`${getStatus(item)}`) }}
                </v-chip>
              </template>

              <template v-slot:item.actions="{ item }">
                <v-btn :disabled="item.paid" @click="openMonthlyDialog(item.id)">{{ $t('pay') }}</v-btn>
              </template>
            </v-data-table>
          </v-tab-item>

          <v-tab-item>
            <v-data-table
                :headers="transactions.headers"
                :items="transactions.items"
                :options.sync="transactions.options"
                :server-items-length="transactions.total"
                :loading="transactions.loading">

              <template v-slot:item.date="{ item }">
                <date-picker :disabled="true" v-model="item.date" :compact-time="true" />
              </template>

              <template v-slot:item.debit="{ item }">
                <currency-field v-if="item.type === 'CREDIT'" :disabled="true" v-model="item.amount" />
              </template>

              <template v-slot:item.credit="{ item }">
                <currency-field v-if="item.type === 'DEBIT'" :disabled="true" v-model="item.amount" />
              </template>

              <template v-slot:item.balance="{ item }">
                <currency-field :disabled="true" v-model="item.balance" />
              </template>
            </v-data-table>
          </v-tab-item>
        </v-tabs>
      </v-card-text>
    </v-card>

    <v-dialog v-model="monthlies.dialog.open" max-width="600px">
      <validation-observer ref="monthlyObserver" v-slot="{ invalid }">
        <form @submit.prevent="payMonthly">
          <v-card>
            <v-card-title>
                <span class="headline">
                  {{ $t('payMonth') }}
                </span>
            </v-card-title>

            <v-card-text>
              <v-alert
                  v-if="monthlies.dialog.error"
                  type="error">
                {{ monthlies.dialog.error }}
              </v-alert>

              <v-switch
                  v-model="monthlies.dialog.model.createTransaction"
                  :label="$t('creditAccountAndCreateTransactoin')" />

              <v-textarea v-if="monthlies.dialog.model.createTransaction"
                          v-model="monthlies.dialog.model.description"
                          :label="$t('description')"
                          rows="2" />
            </v-card-text>
            <v-card-actions>
              <v-btn @click="closeTransactionDialog= false">{{ $t('cancel') }}</v-btn>
              <v-btn type="submit" :disabled="invalid">{{ $t('ok') }}</v-btn>
            </v-card-actions>
          </v-card>
        </form>
      </validation-observer>
    </v-dialog>

    <v-dialog v-model="transactions.dialog.open" max-width="600px">
      <validation-observer ref="transactionObserver" v-slot="{ invalid }">
        <form @submit.prevent="saveTransaction">
          <v-card>
            <v-card-title>
              <span class="headline">
                {{ $t('createTransaction') }}
              </span>
            </v-card-title>
            <v-card-text>
              <v-alert v-if="transactions.dialog.error" type="error">{{ transactions.dialog.error }}</v-alert>

              <v-radio-group v-model="transactions.dialog.model.type" row>
                <v-radio :label="$t('deposit')" value="CREDIT" />
                <v-radio :label="$t('withdraw')" value="DEBIT" />
              </v-radio-group>

              <currency-field
                  v-model="transactions.dialog.model.amount"
                  :label="$t('amount')"
                  rules="required|numeric|min_value:1000" />

              <v-textarea rows="2" :label="$t('description')" v-model="transactions.dialog.model.description" />
            </v-card-text>
            <v-card-actions>
              <v-btn @click="closeTransactionDialog">{{ $t('cancel') }}</v-btn>
              <v-btn type="submit" :disabled="invalid">{{ $t('ok') }}</v-btn>
            </v-card-actions>
          </v-card>
        </form>
      </validation-observer>
    </v-dialog>
  </v-container>
</template>

<script>
import AccountService from '../../services/accounts'
import MemberPicker from '../../components/MemberPicker'
import CurrencyField from '../../components/CurrencyField'
import DatePicker from '../../components/DatePicker'
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default {
  components: {
    MemberPicker,
    ValidationObserver,
    ValidationProvider,
    CurrencyField,
    DatePicker
  },
  metaInfo () {
    return {
      title: this.$t('accountDetailsTitle', [this.accountId])
    }
  },

  props: {
    accountId: {
      type: [Number, String],
      required: true
    },
  },

  data () {
    return {
      loading: false,
      tabs: null,
      account: {
        loading: false,
        member: undefined,
        balance: undefined,
        monthlyAmount: undefined,
      },
      monthlies: {
        total: 0,
        items: [],
        options: {
          sortBy: ['id'],
          sortAscending: [false]
        },
        headers: [
          { text: this.$t('date'), value: 'date', sortable: false },
          { text: this.$t('amount'), value: 'amount', sortable: false },
          { text: this.$t('status'), value: 'status', sortable: false },
          { text: this.$t('actions'), value: 'actions', sortable: false },
        ],
        dialog: {
          open: false,
          error: undefined,
          monthlyId: undefined,
          model: {
            createTransaction: true,
            description: undefined,
          },
        }
      },
      transactions: {
        total: 0,
        items: [],
        options: {
          sortBy: ['id'],
          sortAscending: [false]
        },
        headers: [
          { text: this.$t('date'), value: 'date', sortable: false },
          { text: this.$t('description'), value: 'description', sortable: false },
          { text: this.$t('debtor'), value: 'debit', sortable: false },
          { text: this.$t('creditor'), value: 'credit', sortable: false },
          { text: this.$t('balance'), value: 'balance', sortable: false }
        ],
        dialog: {
          open: false,
          error: undefined,
          model: {
            amount: undefined,
            type: undefined,
            date: undefined,
            description: undefined,
          },
        }
      },
    }
  },

  computed: {},

  mounted () {
    this.loadInformation()
  },

  methods: {
    async loadInformation () {
      try {
        this.loading = true
        this.account = await AccountService.getAccount(this.accountId);
        await this.loadTransactions()
        await this.loadMonthlies()
      } catch (e) {
        await this.$router.push({ path: '/accounts' })
      } finally {
        this.loading = false
      }
    },

    async loadMonthlies () {
      this.monthlies.loading = true
      try {
        const { sortBy, sortAscending, page, itemsPerPage } = this.monthlies.options

        const params = {
          accountId: this.accountId,
          page: page - 1,
          size: itemsPerPage
        }

        if (sortBy.length > 0) {
          params.sort = sortBy.map((field, index) => `${field},${sortAscending[index] ? 'asc' : 'desc'}`)
        }

        const response = await AccountService.loadMonthlies(params)
        this.monthlies.items = response.content
        this.monthlies.total = response.totalElements
      } finally {
        this.monthlies.loading = false
      }
    },

    getStatus (monthly) {
      return monthly.paid ? 'paid' : 'unpaid'
    },

    getColor (monthly) {
      return monthly.paid ? 'gray' : 'red'
    },

    async payMonthly () {
      try {
        this.monthlies.dialog.error = undefined
        await AccountService.payMonthly({
          accountId: this.accountId,
          monthlyId: this.monthlies.dialog.monthlyId,
          createTransaction: this.monthlies.dialog.model.createTransaction,
          description: this.monthlies.dialog.model.description,
        })
        await this.loadInformation()
        this.closeMonthlyDialog()
      } catch (e) {
        this.transactions.dialog.error = e.message
      }
    },

    openMonthlyDialog (monthlyId) {
      this.monthlies.dialog.open = true
      this.monthlies.dialog.monthlyId = monthlyId
    },

    closeMonthlyDialog () {
      this.monthlies.dialog.open = false
      this.$refs.monthlyObserver.reset()
      this.monthlies.dialog.monthlyId = undefined
      this.monthlies.dialog.model = {
        createTransaction: true,
        description: undefined,
      }
    },

    async loadTransactions () {
      this.transactions.loading = true
      try {
        const { sortBy, sortAscending, page, itemsPerPage } = this.transactions.options

        const params = {
          accountId: this.accountId,
          page: page - 1,
          size: itemsPerPage
        }

        if (sortBy.length > 0) {
          params.sort = sortBy.map((field, index) => `${field},${sortAscending[index] ? 'asc' : 'desc'}`)
        }

        const response = await AccountService.loadTransactions(params)
        this.transactions.items = response.content
        this.transactions.total = response.totalElements
      } finally {
        this.transactions.loading = false
      }
    },

    closeTransactionDialog () {
      this.transactions.dialog.open = false
      this.transactions.dialog.model = {
        type: undefined,
        amount: undefined,
        description: undefined,
      }
      this.$refs.transactionObserver.reset()
    },

    async saveTransaction () {
      try {
        this.transactions.dialog.error = undefined
        await AccountService.saveTransactions({
          accountId: this.accountId,
          type: this.transactions.dialog.model.type,
          amount: this.transactions.dialog.model.amount,
          description: this.transactions.dialog.model.description,
        })
        await this.loadInformation()
        this.closeTransactionDialog()
      } catch (e) {
        this.transactions.dialog.error = e.message
      }
    }
  }
}
</script>

<style scoped>

</style>
