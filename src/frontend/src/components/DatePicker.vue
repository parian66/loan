<template>
  <div>
    <validation-provider
      v-if="!disabled"
      v-slot="{ errors }"
      :name="label"
      :rules="rules">
      <v-text-field
        :id="`field${_uid}`"
        v-model="cmpValue"
        v-bind:label="label"
        v-bind:placeholder="placeholder"
        v-bind:readonly="true"
        v-bind:error-messages="errors" />
      <VuePersianDatetimePicker
        v-model="cmpValue"
        :format="dateFormat"
        :type="compactTime ? 'datetime':'date'"
        :auto-submit="autoSubmit"
        :compact-time="compactTime"
        :element="`field${_uid}`" />
    </validation-provider>

    <div v-else>
      {{ label ? label + ': ' : '' }} {{ format }}
    </div>
  </div>
</template>

<script>
import moment from 'moment-jalaali'
import { ValidationProvider } from 'vee-validate'
import VuePersianDatetimePicker from 'vue-persian-datetime-picker'

export default {
  name: 'DatePicker',
  components: { ValidationProvider, VuePersianDatetimePicker },
  model: { prop: 'value', event: 'input' },
  props: {
    value: {
      type: [Date, String],
    },
    rules: {
      type: String,
    },
    label: {
      type: String,
    },
    placeholder: {
      type: String,
      default: undefined
    },
    disabled: {
      type: Boolean,
      default: false
    },
    autoSubmit: {
      type: Boolean,
      default: true
    },
    compactTime: {
      type: Boolean,
      default: false
    },
    errorMessages: {
      type: [Array, String],
      default: () => []
    },
  },
  computed: {
    dateFormat () {
      return 'jYYYY/jM/jD' + (this.compactTime ? ' - HH:mm:ss' : '')
    },

    cmpValue: {
      get: function () {
        return this.format
      },
      set: function (newValue) {
        this.$emit('input', moment(newValue, this.dateFormat).toDate());
      }
    },
    format () {
      return this.value && moment(this.value).format(this.dateFormat)
    }
  },
}
</script>
