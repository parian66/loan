import vuetify from 'vuetify/lib/locale/fa'
import VeeValidate from 'vee-validate/dist/locale/fa.json';

export default {
  $vuetify: { ...vuetify },
  validation: VeeValidate.messages,
  actions: 'عملیات',
  amount: 'مبلغ',
  app: 'نسیم مهر',
  balances: 'مانده ها',
  cancel: 'انصراف',
  commission: 'کارمزد',
  commissionRate: 'نرخ کارمزد',
  dashboard: 'داشبورد',
  date: 'تاریخ',
  description: 'توضیحات',
  deleteConfirmation: 'آیا اطمینان دارید که این مورد را می خواهید حذف کنید؟',
  dir: 'rtl',
  editMember: 'ویرایش عضو',
  errors: {
    invalidUsernameOrPassword: 'نام کاربری یا کلمه عبور نا معتبر است.'
  },
  firstInstallmentDate: 'اولین سررسید',
  firstName: 'نام',
  home: 'خانه',
  installmentCount: 'تعداد قسط',
  irr: 'ریال',
  itemsPerPage: 'تعداد در صفحه',
  lastName: 'نام خانوادگی',
  loan: 'وام',
  loanDetailsTitle: 'وام شماره {0}',
  loans: 'وام ها',
  login: 'ورود',
  logout: 'خروج',
  management: 'مدیریت',
  member: 'عضو',
  members: 'اعضا',
  nationalCode: 'کد ملی',
  newLoan: 'وام جدید',
  newMember: 'هضو جدید',
  nextInstallmentDate: 'سررسید بعدی',
  lastInstallmentDate: 'آخرین سررسید',
  number: 'شماره',
  ok: 'تایید',
  password: 'کلمه عبور',
  phoneNumber: 'شماره همراه',
  principal: 'اصل',
  rate: 'نرخ',
  save: 'ذخیره',
  search: 'جستجو',
  searchMember: 'جستجوی عضو',
  settings: 'تنظیمات',
  signInToYourAccount: 'به حساب خود وارد شوید',
  status: 'وضعیت',
  username: 'نام کاریری',
  remain: 'باقیمانده',
  paid: 'پرداخت شده',
  paidInstallmentCount: 'تعداد قسط پرداخت شده',
  remainInstallmentCount: 'تعداد قسط باقیمانده',
  howManyInstallmentDoYouWantToRepay: 'چند قسط میخواهید باز پرداخت کنید؟',
  back: 'برگشت',
  repay: 'باز پرداخت',
  loanStatus: {
    NEW: 'جدید',
    OK: 'عادی',
    OVERDUE: 'سررسید شده',
    DONE: 'تسویه شده'
  },
  installmentStatus: {
    OK: 'سررسید نشده',
    OVERDUE: 'سررسید شده',
    DONE: 'پرداخت شده'
  }
}
