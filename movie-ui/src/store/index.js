import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import dict from './modules/dict'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import settings from './modules/settings'
import getters from './getters'

import delete_actors from './modules/delete_actors'
import delete_directors from './modules/delete_directors'
import increase_actors from './modules/increase_actors'
import increase_directors from './modules/increase_directors'



Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    dict,
    user,
    tagsView,
    permission,
    settings,
    delete_actors,
    delete_directors,
    increase_actors,
    increase_directors
  },
  getters
})

export default store
