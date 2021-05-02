<template>
  <div>
    <div v-if="!disabled">
      <v-text-field
          :id="`field${_uid}`"
          v-model="cmpValue"
          v-bind:label="label"
          v-bind:placeholder="placeholder"
          v-bind:readonly="true"
          v-bind:error-messages="errorMessages" />
      <VuePersianDatetimePicker
          v-model="cmpValue"
          :auto-submit="true"
          :element="`field${_uid}`" />
    </div>

    <div v-else>
      {{ label ? label + ': ' : '' }} {{ format }}
    </div>
  </div>
</template>

<script>
import moment from 'moment-jalaali'
import VuePersianDatetimePicker from 'vue-persian-datetime-picker'

export default {
  name: 'DatePicker',
  components: { VuePersianDatetimePicker },
  model: { prop: 'value', event: 'input' },
  props: {
    value: {
      type: [Date, String],
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
    errorMessages: {
      type: [Array, String],
      default: () => []
    },
  },
  computed: {
    cmpValue: {
      get: function () {
        return this.format
      },
      set: function (newValue) {
        this.$emit('input', moment(newValue, 'jYYYY/jM/jD').toDate());
      }
    },
    format () {
      return this.value && moment(this.value).format('jYYYY/jM/jD')
    }
  },
}
</script>
