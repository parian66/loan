<template>
  <v-card>
    <v-card-title>
      <span class="headline">{{ $t('newLoan') }}</span>
    </v-card-title>
    <v-card-text>
      <v-alert v-if="error" type="error">{{ error }}</v-alert>
      <validation-observer ref="observer" v-slot="{ invalid }">
        <form @submit.prevent="save">
          <v-container>
            <v-row>
              <v-col>
                <MemberPicker v-model="memberId" />
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <validation-provider
                    v-slot="{ errors }"
                    :name="$t('amount')"
                    rules="required">
                  <currency-field
                      v-model="amount"
                      :label="$t('amount')"
                      :error-messages="errors" />
                </validation-provider>
              </v-col>
              <v-col>
                <validation-provider
                    v-slot="{ errors }"
                    :name="$t('commissionRate')"
                    rules="required|numeric|min_value:1|max_value:10">
                  <v-text-field
                      v-model="rate"
                      :label="$t('commissionRate')"
                      suffix="%"
                      :error-messages="errors" />
                </validation-provider>
              </v-col>
              <v-col>
                <currency-field
                    v-model="principal"
                    :label="$t('principal')"
                    :readonly="true" />
              </v-col>
              <v-col>
                <currency-field
                    v-model="commission"
                    :label="$t('commission')"
                    :readonly="true" />
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <validation-provider
                    v-slot="{ errors }"
                    :name="$t('firstInstallmentDate')"
                    rules="required">
                  <date-picker
                      v-model="firstInstallmentDate"
                      :label="$t('firstInstallmentDate')"
                      :error-messages="errors" />
                </validation-provider>
              </v-col>

              <v-col>
                <validation-provider
                    v-slot="{ errors }"
                    :name="$t('installmentCount')"
                    rules="required|numeric|min_value:1|max_value:10">
                  <v-text-field
                      v-model="installmentCount"
                      :label="$t('installmentCount')"
                      :error-messages="errors" />
                </validation-provider>
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <v-textarea
                    rows="1"
                    :label="$t('description')"
                    v-model="description" />
              </v-col>
            </v-row>
          </v-container>
          <v-simple-table v-if="!invalid">
            <template v-slot:default>
              <thead>
              <tr>
                <th>{{ $t('number') }}</th>
                <th>{{ $t('amount') }}</th>
                <th>{{ $t('date') }}</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="item in installments" :key="item.name">
                <td>{{ item.number }}</td>
                <td>
                  <currency-field v-model="item.amount" :disabled="true" />
                </td>
                <td>
                  <date-picker v-model="item.date" :disabled="true" />
                </td>
              </tr>
              </tbody>
            </template>
          </v-simple-table>

          <v-btn @click="back">{{ $t('back') }}</v-btn>
          <v-btn type="submit" :disabled="invalid">
            {{ $t('save') }}
          </v-btn>
        </form>
      </validation-observer>
    </v-card-text>
  </v-card>
</template>

<script>
import moment from 'moment-jalaali'
import LoanService from '../../services/loans'
import MemberPicker from '../../components/MemberPicker'
import CurrencyField from '../../components/CurrencyField'
import DatePicker from '../../components/DatePicker'
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default {
  name: 'NewLoan',
  components: { ValidationObserver, ValidationProvider, MemberPicker, CurrencyField, DatePicker },
  data () {
    return {
      error: undefined,
      rate: undefined,
      amount: undefined,
      memberId: undefined,
      installmentCount: undefined,
      firstInstallmentDate: undefined,
      description: undefined,
      memberLookup: {
        loading: false,
        search: undefined,
        items: []
      }
    }
  },

  computed: {
    commission () {
      return Math.round(this.amount * this.rate / 100)
    },
    principal () {
      return this.amount - this.commission
    },
    installments () {
      const date = this.firstInstallmentDate
      const count = this.installmentCount
      const amount = Math.round(this.amount / count)
      const lastAmount = this.amount - (count - 1) * amount
      return Array.from({ length: count }, (_, i) => ({
        number: i + 1,
        amount: i !== count - 1 ? amount : lastAmount,
        date: moment(date).add(i, 'jM').toDate()
      }))
    }
  },

  methods: {
    back () {
      this.$router.push({ name: 'LoanList' })
    },
    async save () {
      try {
        this.error = undefined
        await LoanService.createLoan({
          rate: this.rate,
          amount: this.amount,
          memberId: this.memberId,
          installmentCount: this.installmentCount,
          firstInstallmentDate: this.firstInstallmentDate,
          description: this.description,
        })
        this.back()
      } catch (e) {
        this.error = e.message
      }
    },
  }
}
</script>

<style scoped>

</style>
