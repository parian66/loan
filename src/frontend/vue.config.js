module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080/',
      },
      '/auth': {
        target: 'http://localhost:8080/',
      }
    }
  },
  transpileDependencies: [
    'vuetify'
  ],
  lintOnSave: false
}
