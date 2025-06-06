export default {
  namespaced: true,
  state: {
    moviesLoaded: false,
    lastDeletedActorIds: [],
    dataUpdatedFlag: false   
  },
  mutations: {
    SET_DELETED_ACTORS(state, ids) {
      state.lastDeletedActorIds = ids
      state.dataUpdatedFlag = true
    },
    CLEAR_DATA_UPDATED_FLAG(state) {
      state.dataUpdatedFlag = false
    },
    SET_MOVIES_LOADED(state, loaded) {
      state.moviesLoaded = loaded
    },
  }
}
