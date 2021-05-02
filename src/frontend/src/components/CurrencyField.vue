<template>
  <div>
    <vuetify-money
        v-if="!disabled"
        v-model="cmpValue"
        v-bind:label="label"
        v-bind:placeholder="placeholder"
        v-bind:readonly="readonly"
        v-bind:valueWhenIsEmpty="null"
        v-bind:options="options"
        v-bind:error-messages="errorMessages" />
    <template v-else>
      {{ label ? label + ': ' : '' }} {{ humanFormat }}
    </template>
  </div>
</template>

<script>
export default {
  name: 'CurrencyField',
  model: { prop: 'value', event: 'input' },
  props: {
    value: {
      type: [Number],
    },
    label: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: undefined
    },
    disabled: {
      type: Boolean,
      default: false
    },
    readonly: {
      type: Boolean,
      default: false
    },
    errorMessages: {
      type: [Array, String],
      default: () => []
    },
  },
  data () {
    return {
      options: {
        suffix: this.$t('irr'),
        precision: 0
      }
    }
  },
  computed: {
    cmpValue: {
      get: function () {
        return this.value;
      },
      set: function (newValue) {
        this.$emit('input', Number.parseInt(newValue));
      }
    },
    humanFormat () {
      if (isNaN(this.value)) {
        return '';
      }

      let format = Number(this.value).toLocaleString(this.options.locale, {
        maximumFractionDigits: this.options.precision,
        minimumFractionDigits: this.options.precision
      })
      return `${format} ${this.options.suffix}`;
    }
  },
};
</script>
