// 此vm参数为页面的实例，可以通过它引用vuex中的变量
module.exports = (vm) => {
    // 初始化请求配置
    uni.$u.http.setConfig((config) => {
        /* config 为默认全局配置*/
        config.baseURL = 'https://www.mygirlfriends.cn'; /* 根域名 生产环境*/
		    // config.baseURL = 'http://localhost:7878'; /* 根域名 开发环境*/
        return config
    })
	
	// 请求拦截
	// uni.$u.http.interceptors.request.use((config) => { // 可使用async await 做异步操作
	// 	if (config.method != 'GET') { //GET请求不拦截
	// 		if(!uni.getStorageSync('token') && config.custom.istoken != true ) { //其他请求如果没有token直接拦截
	// 			uni.$u.toast('请在登陆后进行操作')
	// 			console.log('拦截')
 //        setTimeout(() => {
 //          uni.navigateTo({ url: '/subPackageA/pages/me/login' })
 //        }, 1000)
	// 			return Promise.reject(config)
	// 		}
	// 	} 
	// 	else if(config.custom.needLogin == true && !uni.getStorageSync('token')) return new Promise(() => {})
 //    // console.log(`[info] request url: ${JSON.stringify(config)}`)
    
 //    return config 
	// }, config => { // 可使用async await 做异步操作
	//     return Promise.reject(config)
	// })
	
	// 响应拦截
	uni.$u.http.interceptors.response.use((response) => { /* 对响应成功做点什么 可使用async await 做异步操作*/
		const data = response.data

		// 自定义参数
		const custom = response.config?.custom
		if (data.code !== 0 && data.code !==10004) {
			// 如果没有显式定义custom的toast参数为false的话，默认对报错进行toast弹出提示
			if (custom.toast !== false) {
				//uni.$u.toast(data.message)
				console.error('error',response);
			}
			return Promise.reject(response)
		}
		return data.data === undefined ? {} : data.data
	}, (response) => { 
		// 对响应错误做点什么 （statusCode !== 200）
		return Promise.reject(response)
	})
}