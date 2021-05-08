<template>
  <v-container
      fluid
      tag="section">
    <v-card>
      <v-card-title>
        {{$t('loans')}}
        <v-spacer />
        <v-btn @click="$router.push({name:'NewLoan'})">{{ $t('newLoan') }}</v-btn>
      </v-card-title>

      <v-data-table
          :headers="headers"
          :items="items"
          :options.sync="options"
          :server-items-length="total"
          :loading="loading">

        <template v-slot:item.member="{ item }">
          {{ item.member.lastName }} - {{ item.member.firstName }}
        </template>

        <template v-slot:item.amount="{ item }">
          <currency-field :disabled="true" v-model="item.amount" />
        </template>

        <template v-slot:item.nextInstallmentDate="{ item }">
          <date-picker v-model="item.nextInstallmentDate" :disabled="true" />
        </template>

        <template v-slot:item.status="{ item }">
          <v-chip :color="getColor(item.status)">
            {{ $t(`loanStatus.${item.status}`) }}
          </v-chip>
        </template>

        <template v-slot:item.actions="{ item }">
          <v-btn @click="$router.push({name:'LoanDetails',params:{loanId:item.id}})">
            {{ $t('view') }}
          </v-btn>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
import LoanService from '../../services/loans'
import MemberPicker from '../../components/MemberPicker'
import CurrencyField from '../../components/CurrencyField'
import DatePicker from '../../components/DatePicker'
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default {
  name: 'LoanList',
  components: {
    MemberPicker,
    ValidationObserver,
    ValidationProvider,
    CurrencyField,
    DatePicker
  },
  metaInfo () {
    return {
      title: this.$t('loans')
    }
  },

  data () {
    return {
      total: 0,
      items: [],
      loading: false,
      options: {
        sortBy: ['nextInstallmentDate'],
        sortAscending: [false]
      },
      headers: [
        { text: this.$t('code'), value: 'id', sortable: false },
        { text: this.$t('member'), value: 'member', sortable: false },
        { text: this.$t('amount'), value: 'amount', sortable: false },
        { text: `${this.$t('rate')} %`, value: 'rate', sortable: false },
        { text: this.$t('nextInstallmentDate'), value: 'nextInstallmentDate', sortable: true },
        { text: this.$t('status'), value: 'status', sortable: true },
        { text: this.$t('actions'), value: 'actions', sortable: false }
      ]
    }
  },

  mounted () {
    this.loadLoans()
  },

  methods: {
    async loadLoans () {
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

        const response = await LoanService.loadLoans(params)
        this.items = response.content
        this.total = response.totalElements
      } finally {
        this.loading = false
      }
    },

    getColor (status) {
      if (status === 'NEW') return 'green'
      if (status === 'OK') return 'blue'
      if (status === 'OVERDUE') return 'red'
      else return 'gray'
    }
  }
}
</script>

<style scoped>

</style>
