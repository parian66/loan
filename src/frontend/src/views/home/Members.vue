<template>
  <v-container
      fluid
      tag="section">
    <v-card>
      <v-card-title>
        {{ $t('members') }}
        <v-spacer />
        <v-btn @click="dialog = true">{{ $t('newMember') }}</v-btn>
      </v-card-title>
      <v-card-title>
        <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            :label="$t('search')"
            single-line
            hide-details />
      </v-card-title>
      <v-card-actions>
        <v-dialog v-model="dialog" max-width="500px">
          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <validation-observer ref="observer" v-slot="{ invalid }">
                <form @submit.prevent="save">
                  <validation-provider
                      v-slot="{ errors }"
                      :name="$t('firstName')"
                      rules="required">
                    <v-text-field
                        required v-model="editedItem.firstName"
                        :label="$t('firstName')"
                        :error-messages="errors" />
                  </validation-provider>

                  <validation-provider
                      v-slot="{ errors }"
                      :name="$t('lastName')"
                      rules="required">
                    <v-text-field
                        required
                        v-model="editedItem.lastName"
                        :label="$t('lastName')"
                        :error-messages="errors" />
                  </validation-provider>

                  <validation-provider
                      v-slot="{ errors }"
                      :name="$t('nationalCode')"
                      rules="required|digits:10">
                    <v-text-field
                        counter="10"
                        v-model="editedItem.nationalCode"
                        :label="$t('nationalCode')"
                        :error-messages="errors" />
                  </validation-provider>

                  <validation-provider
                      v-slot="{ errors }"
                      :name="$t('phoneNumber')"
                      rules="required">
                    <v-text-field
                        required
                        v-model="editedItem.phoneNumber"
                        :label="$t('phoneNumber')"
                        :error-messages="errors" />
                  </validation-provider>

                  <v-btn text color="blue darken-1" @click="close">{{ $t('cancel') }}</v-btn>
                  <v-btn text color="blue darken-1" class="mr-4" type="submit" :disabled="invalid">
                    {{ $t('save') }}
                  </v-btn>
                </form>
              </validation-observer>
            </v-card-text>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="headline">{{ $t('deleteConfirmation') }}</v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col>{{ $t('firstName') }}:</v-col>
                  <v-col>{{ editedItem.firstName }}</v-col>
                </v-row>
                <v-row>
                  <v-col>{{ $t('lastName') }}:</v-col>
                  <v-col>{{ editedItem.lastName }}</v-col>
                </v-row>
                <v-row>
                  <v-col>{{ $t('nationalCode') }}:</v-col>
                  <v-col>{{ editedItem.nationalCode }}</v-col>
                </v-row>
                <v-row>
                  <v-col>{{ $t('phoneNumber') }}:</v-col>
                  <v-col>{{ editedItem.phoneNumber }}</v-col>
                </v-row>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">{{ $t('cancel') }}</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm">{{ $t('ok') }}</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-card-actions>
      <v-data-table
          :headers="headers"
          :items="items"
          :options.sync="options"
          :server-items-length="total"
          :search="search"
          :loading="loading">

        <template v-slot:item.actions="{ item }">
          <v-btn @click="editItem(item.id)">
            {{ $t('edit') }}
          </v-btn>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
import MemberService from '../../services/members'
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default {
  name: 'Members',
  components: {
    ValidationProvider,
    ValidationObserver
  },
  metaInfo () {
    return {
      title: this.$t('members')
    }
  },
  data () {
    return {
      search: undefined,
      dialog: false,
      dialogDelete: false,
      total: 0,
      items: [],
      loading: false,
      options: {
        sortBy: ['lastName', 'firstName'],
        sortAscending: [true, true]
      },
      headers: [
        { text: this.$t('code'), value: 'id', sortable: false },
        { text: this.$t('lastName'), value: 'lastName', sortable: true },
        { text: this.$t('firstName'), value: 'firstName', sortable: true },
        { text: this.$t('nationalCode'), value: 'nationalCode', sortable: false },
        { text: this.$t('phoneNumber'), value: 'phoneNumber', sortable: false },
        { text: this.$t('actions'), value: 'actions', sortable: false }
      ],
      editedId: undefined,
      editedItem: {
        firstName: undefined,
        lastName: undefined,
        nationalCode: undefined,
        phoneNumber: undefined
      }
    }
  },
  computed: {
    formTitle () {
      return this.editedId === -1 ? this.$t('newMember') : this.$t('editMember')
    }
  },
  watch: {
    search: {
      handler () {
        this.options.page = 1
        this.loadMembers()
      }
    },
    options: {
      handler () {
        this.loadMembers()
      },
      deep: true
    },
    dialog (val) {
      val || this.close()
    },
    dialogDelete (val) {
      val || this.closeDelete()
    }
  },

  mounted () {
    this.loadMembers()
  },

  methods: {
    async loadMembers () {
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
        if (this.search) {
          params.search = this.search
        }

        const response = await MemberService.loadMembers(params)
        this.items = response.content
        this.total = response.totalElements
      } finally {
        this.loading = false
      }
    },
    async editItem (id) {
      this.editedId = id
      this.editedItem = await MemberService.getMember(id)
      this.dialog = true
    },

    async deleteItem (id) {
      this.editedId = id
      this.editedItem = await MemberService.getMember(id)
      this.dialogDelete = true
    },

    async deleteItemConfirm () {
      await MemberService.deleteMember(this.editedId)
      await this.loadMembers()
      this.closeDelete()
    },

    close () {
      this.dialog = false
      this.$refs.observer.reset()
      this.editedId = undefined
      this.editedItem = {
        firstName: undefined,
        lastName: undefined,
        nationalCode: undefined,
        phoneNumber: undefined
      }
    },

    closeDelete () {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedId = undefined
      })
    },

    async save () {
      if (this.editedId) {
        await MemberService.updateMember(this.editedId, this.editedItem)
      } else {
        await MemberService.createMember(this.editedItem)
      }
      await this.loadMembers()
      this.close()
    }
  }
}
</script>

<style scoped>

</style>
