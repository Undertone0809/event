import App from './App'
import Vue from 'vue'
import store from './store'
import uView from '@/uni_modules/uview-ui'
import common from "./util/common"
import broadcast from "./util/broadcast"
import zeeUtils from 'util/zeeUtils'

Vue.prototype.$disp = broadcast
Vue.prototype.$zee = zeeUtils
Vue.prototype.$common = common
Vue.prototype.$store = store
Vue.use(uView)

Vue.config.productionTip = false
App.mpType = 'app'

const app = new Vue({
	store,
    ...App
})

// 引入请求封装，将app参数传递到配置中
require('@/common/request.js')(app)

app.$mount()
