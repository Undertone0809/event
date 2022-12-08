import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
	state:{
		hasLogin: false,
		userId: '',
		fresh:false,
		tabbarValue: 0,
		sendPageStatus:true
	},
	mutations: {
		setLoginStatus(state,b) {
			state.hasLogin = b
		},
		setUserId(state,id) {
			state.userId = id
		},
		setFresh(state,b) {
			state.fresh = b
		},
		refreshTabbar(state,b) {
			state.tabbarValue = b
		},
		setSendPageStatus(state,b) {
			state.sendPageStatus = b
		},
	}
})
