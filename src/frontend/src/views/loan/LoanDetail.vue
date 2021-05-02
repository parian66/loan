<template>
  <v-container
      fluid
      tag="section">
    <v-card>
      <v-card-title>
        {{ $t('loanDetailsTitle', [loanId]) }}
      </v-card-title>
      <v-card-text v-if="loan.member">
        <v-row>
          <v-col>
            {{ $t('member') }}: {{ loan.member.firstName }} - {{ loan.member.lastName }}
          </v-col>
          <v-col>
            <currency-field :disabled="true" :label="$t('amount')" v-model="loan.amount" />
          </v-col>
          <v-col>
            {{ $t('rate') }}: {{ loan.rate }} %
          </v-col>
          <v-col>
            {{ $t('status') }}: {{ $t(`loanStatus.${loan.status}`) }}
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <currency-field :disabled="true" :label="$t('principal')" v-model="principal" />
          </v-col>
          <v-col>
            <currency-field :disabled="true" :label="$t('commission')" v-model="commission" />
          </v-col>
          <v-col>
            <currency-field :disabled="true" :label="$t('paid')" v-model="paid" />
          </v-col>
          <v-col>
            <currency-field :disabled="true" :label="$t('remain')" v-model="remain" />
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            {{ $t('installmentCount') }}: {{ loan.installmentCount }}
          </v-col>
          <v-col>
            {{ $t('paidInstallmentCount') }}: {{ paidCount }}
          </v-col>
          <v-col>
            <date-picker v-model="loan.firstInstallmentDate" :disabled="true" :label="$t('firstInstallmentDate')" />
          </v-col>
          <v-col>
            <date-picker v-model="loan.nextInstallmentDate" :disabled="true" :label="$t('nextInstallmentDate')" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-text>
        <v-simple-table>
          <template v-slot:default>
            <thead>
            <tr>
              <th>{{ $t('number') }}</th>
              <th>{{ $t('amount') }}</th>
              <th>{{ $t('date') }}</th>
              <th>{{ $t('status') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in loan.installments" :key="item.id">
              <td>{{ item.number }}</td>
              <td>
                <currency-field v-model="item.amount" :disabled="true" />
              </td>
              <td>
                <date-picker v-model="item.date" :disabled="true" />
              </td>
              <td>
                <v-chip :color="getColor(item)">
                  {{ $t(`installmentStatus.${getStatus(item)}`) }}
                </v-chip>
              </td>
            </tr>
            </tbody>
          </template>
        </v-simple-table>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="$router.push({path:'/loans'})">
          {{ $t('back') }}
        </v-btn>
        <v-btn @click="dialog.open = true" :disabled="!remainCount">
          {{ $t('repay') }}
        </v-btn>
      </v-card-actions>
    </v-card>
    <v-dialog v-model="dialog.open" persistent max-width="600px">
      <validation-observer ref="observer" v-slot="{ invalid }">
        <v-card>
          <v-card-title>
            <span class="headline">
              {{ $t('repay') }}
            </span>
          </v-card-title>
          <v-card-text>
            <v-row v-if="dialog.error">
              <v-col>
                <v-alert type="error">{{ dialog.error }}</v-alert>
              </v-col>
            </v-row>
            <v-row justify="center">
              <v-col cols="6">
                <validation-provider
                    v-slot="{ errors }"
                    :name="$t('installmentCount')"
                    :rules="`required|numeric|min_value:1|max_value:${remainCount}`">
                  <v-text-field
                      v-model="dialog.count"
                      :label="$t('howManyInstallmentDoYouWantToRepay')"
                      :error-messages="errors" />
                </validation-provider>
              </v-col>
            </v-row>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="dialog.open = false">{{ $t('cancel') }}</v-btn>
            <v-btn @click="repay" type="submit" :disabled="invalid">{{ $t('ok') }}</v-btn>
          </v-card-actions>
        </v-card>
      </validation-observer>
    </v-dialog>
  </v-container>
</template>

<script>
import moment from 'moment-jalaali'
import LoanService from '../../services/loans'
import MemberPicker from '../../components/MemberPicker'
import CurrencyField from '../../components/CurrencyField'
import DatePicker from '../../components/DatePicker'
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default {
  name: 'Loans',
  components: {
    MemberPicker,
    ValidationObserver,
    ValidationProvider,
    CurrencyField,
    DatePicker
  },
  metaInfo () {
    return {
      title: this.$t('loanDetailsTitle', [this.loanId])
    }
  },

  props: {
    loanId: {
      type: [Number, String],
      required: true
    },
  },

  data () {
    return {
      loading: false,
      loan: {
        member: undefined,
        rate: undefined,
        amount: undefined,
        status: undefined,
        installmentCount: undefined,
        firstInstallmentDate: undefined,
        nextInstallmentDate: undefined,
        lastInstallmentDate: undefined,
        description: undefined,
        installments: [],
      },
      headers: [
        { text: this.$t('number'), value: 'member', sortable: false },
        { text: this.$t('amount'), value: 'amount', sortable: false },
        { text: this.$t('date'), value: 'date', sortable: false },
        { text: this.$t('status'), value: 'status', sortable: false }
      ],
      dialog: {
        open: false,
        count: undefined,
        error: undefined,
      }
    }
  },

  computed: {
    commission () {
      return Math.round(this.loan.amount * this.loan.rate / 100)
    },
    principal () {
      return this.loan.amount - this.commission
    },
    paidCount () {
      return this.loan.installments.filter(installment => installment.paid).length
    },
    remainCount () {
      return this.loan.installmentCount - this.paidCount
    },
    paid () {
      const reducer = (accumulator, installment) => accumulator + installment.amount;
      return this.loan.installments.filter(installment => installment.paid).reduce(reducer, 0)
    },
    remain () {
      return this.loan.amount - this.paid
    },
  },

  mounted () {
    this.loadInformation()
  },

  methods: {
    async loadInformation () {
      try {
        this.loading = true
        this.loan = await LoanService.getLoan(this.loanId);
        this.loan.installments.sort((a, b) => a.number - b.number)
      } catch (e) {
        await this.$router.push({ path: '/loans' })
      } finally {
        this.loading = false
      }
    },

    getStatus (installment) {
      if (installment.paid) return 'DONE'
      if (moment(installment.date).isBefore(moment(), 'day')) return 'OVERDUE'
      else return 'OK'
    },

    getColor (installment) {
      const status = this.getStatus(installment)
      if (status === 'DONE') return 'green'
      if (status === 'OVERDUE') return 'red'
      else return 'gray'
    },

    async repay () {
      try {
        this.dialog.error = undefined
        await LoanService.repay({ id: this.loanId, count: this.dialog.count })
        await this.loadInformation()
        this.dialog.open = false
      } catch (e) {
        this.dialog.error = e.message
      }
    },
  }
}
</script>

<style scoped>

</style>
