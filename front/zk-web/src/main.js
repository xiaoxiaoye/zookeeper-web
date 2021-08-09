import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Vue from 'vue'
import App from './App.vue'
import VueRouter from "vue-router";
import router from './router'


import {getRequest,putRequest,postRequest, deleteRequest} from "@/utils/apis";
Vue.prototype.getRequest = getRequest
Vue.prototype.putRequest = putRequest
Vue.prototype.postRequest = postRequest
Vue.prototype.deleteRequest = deleteRequest

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueRouter)

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
