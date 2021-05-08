import Vue from 'vue'
import VueRouter from 'vue-router'
import authorization from './services/authorization'

// views
const Login = () => import('./views/Login')
const Home = () => import('./views/Home')

// views/home
const Members = () => import('./views/home/Members')
const Management = () => import('./views/home/Management')

// views/loan
const LoanIndex = () => import('./views/loan/Index')
const LoanList = () => import('./views/loan/LoanList')
const LoanDetails = () => import('./views/loan/LoanDetails')
const NewLoan = () => import('./views/loan/NewLoan')

// views/account
const AccountIndex = () => import('./views/account/Index')
const AccountList = () => import('./views/account/AccountList')
const AccountDetails = () => import('./views/account/AccountDetails')

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'hash',
  routes: [
    {
      path: '/',
      redirect: '/accounts',
      name: 'Home',
      component: Home,
      children: [
        {
          path: 'accounts',
          name: 'AccountIndex',
          redirect: '/accounts',
          component: AccountIndex,
          children: [
            {
              path: '',
              name: 'AccountList',
              component: AccountList
            },
            {
              path: ':accountId',
              name: 'AccountDetails',
              props: true,
              component: AccountDetails
            }
          ]
        },
        {
          path: 'loans',
          name: 'LoanIndex',
          redirect: '/loans',
          component: LoanIndex,
          children: [
            {
              path: '',
              name: 'LoanList',
              component: LoanList
            },
            {
              path: ':loanId',
              name: 'LoanDetails',
              props: true,
              component: LoanDetails
            },
            {
              path: 'new',
              name: 'NewLoan',
              component: NewLoan
            },
          ]
        },
        {
          path: 'members',
          name: 'Members',
          component: Members
        },
        {
          path: 'management',
          name: 'Management',
          component: Management
        }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.name !== 'Login' && !authorization.isLoggedIn()) next({ name: 'Login' })
  else next()
})

export default router
