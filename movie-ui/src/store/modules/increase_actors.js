export default {
    namespaced: true,
    state: {
      moviesLoaded: false,
      IncreaseIds: null,
      dataUpdatedFlag: false   
    },
    mutations: {
      SET_INCREASE_ACTORS(state, ids) {
        state.IncreaseIds = ids
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
  