<script>
	function socketInit() {
		let userid = uni.getStorageSync('userInfo').userid
		// 已经登陆
		if (userid) {
			//即时通讯连接服务器
			uni.connectSocket({
				url: 'wss://www.mygirlfriends.cn/websocket/' + userid
			}).then(res => {
				console.log(`[info] connectSocket success: ${JSON.stringify(res)}`)
			}, err => {
				console.log(`[error] connectSocket error: ${JSON.stringify(err)}`)
			})
			//websocket监听函数
			uni.onSocketOpen(res => { console.log('WebSocket连接已打开!') })
			uni.onSocketError(res => {
				uni.showToast({ title: 'WebSocket连接失败!',icon: 'none' })
			})
			uni.onSocketClose(res => {
				console.log('WebSocket 已关闭！')
			}, err => {
				console.log(`[error] closeSocket error: ${JSON.stringify(err)}`)
			})
			//监听WebSocket接受到服务器的消息事件。
			uni.onSocketMessage(res => {
				var getMsg = JSON.parse(res.data)
				var tempMsg = uni.getStorageSync('tempMsg');
				let temp = new Map(tempMsg) //由Array转为Map
				var unreadNum = uni.getStorageSync('unreadNum');
				if (getMsg instanceof Array) { //未读消息
					let msgList = uni.getStorageSync('msgList')
					let len = getMsg.length
					if (len > 0) {
						uni.setStorageSync('tempMsg', getMsg)
						uni.setTabBarBadge({
							index: 3,
							text: String(unreadNum + len)
						})
						uni.setStorageSync('unreadNum', unreadNum + len);
						uni.showToast({
							title: '你有' + len + '条新的未读消息',
							icon: 'none'
						})
					} else {
						if (unreadNum) uni.setTabBarBadge({
							index: 3,
							text: String(unreadNum)
						})
						if (!msgList) { //第一次连接
							uni.setStorageSync('msgList', [])
							uni.setStorageSync('tempMsg', [])
						}
					}
				} else { //实时消息
					//在打开消息页面之前，实时消息都只能先存在缓存tempMsg中,打开消息页面后再与msgList合并
					if (temp.has(getMsg.chatSenderId)) {
						let x = temp.get(getMsg.chatSenderId)
						getMsg.badge = x.badge ? x.badge + 1 : 1
					} else getMsg.badge = 1
					temp.set(getMsg.chatSenderId, getMsg);
					uni.showToast({
						title: '您收到一条消息',
						icon: 'none'
					})
					uni.setTabBarBadge({
						index: 3,
						text: String(unreadNum + 1)
					})
					uni.setStorageSync('tempMsg', [...temp]) //由Map转回Array
					uni.setStorageSync('unreadNum', unreadNum + 1);
				}
			})
		}
	}

	export default {
		onLaunch () {
			console.log('App Launch')
			// 检查网络状态
			uni.getNetworkType({}).then(res => {
				if (res.networkType == 'none') uni.showToast({
					title: '请检查网络状态',
					duration: 2000,
					icon: 'error'
				})
			})
      
      // init
			this.$common.initData()
			socketInit()

      // 计算个人信息是否完善
      this.$common.calPercentage()
		},
		onShow () {
			console.log('App Show')
			uni.onSocketOpen(res => { console.log('WebSocket连接再次打开!') })
			var tid = uni.getStorageSync('tid')
			if (tid) {
				clearTimeout(tid);
				uni.removeStorageSync('tid')
			}      
		},
		onHide () {
			console.log('App Hide')
			// onShow和onHide之间超过20分钟断开连接，避免查看图片时断开连接
			let userid = uni.getStorageSync('userInfo').userid
			if (userid) {
				var tid = setTimeout(() => {
					console.log('[info] 切后台过了20分钟,socket掉了!')
					uni.closeSocket()
				}, 20 * 60 * 1000)
				uni.setStorageSync('tid', tid)
			}
		}
	}
</script>

<style lang="scss">
	@import "@/uni_modules/uview-ui/index.scss";

	html {
		font-size: 16px;
	}

	body {
		margin: 0;
		font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', 'Microsoft Yahei', sans-serif;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
	}

	view,
	image,
	text {
		box-sizing: border-box;
		flex-shrink: 0;
	}

	#app {
		width: 100vw;
		height: 100vh;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
	}

	.flex-col {
		display: flex;
		flex-direction: column;
	}

	.justify-start {
		display: flex;
		justify-content: flex-start;
	}

	.justify-center {
		display: flex;
		justify-content: center;
	}

	.justify-end {
		display: flex;
		justify-content: flex-end;
	}

	.justify-evenly {
		display: flex;
		justify-content: space-evenly;
	}

	.justify-around {
		display: flex;
		justify-content: space-around;
	}

	.justify-between {
		display: flex;
		justify-content: space-between;
	}

	.items-start {
		display: flex;
		align-items: flex-start;
	}

	.items-center {
		display: flex;
		align-items: center;
	}

	.items-end {
		display: flex;
		align-items: flex-end;
	}
</style>
