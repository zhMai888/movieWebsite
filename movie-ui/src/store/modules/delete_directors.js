export default {
    namespaced: true,
    state: {
      moviesLoaded: false,
      lastDeletedDirectorIds: [],
      dataUpdatedFlag: false   
    },
    mutations: {
      SET_DELETED_DIRECTORS(state, ids) {
        state.lastDeletedDirectorIds = ids
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
  