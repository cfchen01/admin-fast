export default {
  namespaced: true,
  state: {
    id: 0,
    name: '',
    nick:''
  },
  mutations: {
    updateId (state, id) {
      state.id = id
    },
    updateName (state, name) {
      state.name = name
    },
    updateNick (state, nick) {
      state.nick = nick
    }
  }
}
