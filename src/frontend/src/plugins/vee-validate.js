import { configure, extend } from 'vee-validate';
import { digits, email, max, max_value, min, min_value, numeric, required } from 'vee-validate/dist/rules';
import i18n from '../i18n';

configure({
  defaultMessage: (field, values) => {
    values._field_ = field;

    return i18n.t(`validation.${values._rule_}`, values);
  }
});

extend('required', required);
extend('email', email);
extend('min', min);
extend('max', max);
extend('min_value', min_value);
extend('max_value', max_value);
extend('digits', digits);
extend('numeric', numeric);
