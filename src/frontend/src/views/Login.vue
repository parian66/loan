<template>
  <v-app>
    <v-main>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card>
              <v-toolbar dark color="primary">
                <v-toolbar-title>
                  {{ $t('login') }}
                </v-toolbar-title>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field
                    name="input-10-1"
                    v-model="username"
                    :label="$t('username')" />
                  <v-text-field
                    name="input-10-1"
                    v-model="password"
                    :label="$t('password')"
                    :type="show ? 'text' : 'password'"
                    :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                    @click:append="show = !show" />
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer />
                <v-btn
                  color="primary"
                  :disabled="isDisabled"
                  @click="login">
                  {{ $t('login') }}
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import authorization from '../../../frontend/src/services/authorization'

export default {
  name: 'Login',
  metaInfo () {
    return {
      title: this.$t('login')
    }
  },
  data () {
    return {
      username: undefined,
      password: undefined,
      show: false,
      loading: false,
      error: undefined
    }
  },

  computed: {
    isDisabled () {
      return !(this.username && this.password && !this.loading)
    }
  },

  created () {
    authorization.logout()
  },

  methods: {
    async login () {
      this.loading = true
      this.error = undefined
      try {
        await authorization.login({ username: this.username, password: this.password })
        await this.$router.push({ path: '/' })
      } catch (error) {
        this.error = this.$t('errors.invalidUsernameOrPassword')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>

</style>
