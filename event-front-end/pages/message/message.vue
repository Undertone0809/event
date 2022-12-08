<template>
	<view>
		<view class="content">
			<template v-if="nologin">
				<nologin></nologin>
			</template>
			<template v-else>
				<view v-if="show">
					<view v-if="msg.length==0" style="margin-top: 38vh;">
						<u-empty mode="message"></u-empty>
					</view>
					<view v-for="(item, index) in msg" :key="index" @click="toChat(item)" @longpress="del(index)">
						<view class="messageItem">
							<!-- 用户头像 -->
							<image :src="item.avatar" mode="aspectFill"></image>
							<view class="itemText">
								<view class="itemTitle">
									<!-- 用户昵称 -->
									<view class="itemName">{{item.openname}}</view>
									<!-- 消息时间 -->
									<view class="itemTime">{{item.chatTime | timeFrom('yyyy-mm-dd')}}</view>
								</view>
								<!-- 消息内容 -->
								<view class="itemMessage">
									<view class="text">{{item.charContext}}</view>
									<view v-if="item.badge" class="badge">{{item.badge}}</view>
								</view>
							</view>
						</view>
					</view>
				</view>
				<image v-else class="loading" src="https://s2.loli.net/2022/01/27/DpnTg8xAEbUrLKy.gif"></image>
			</template>
		</view>
		<tabbar></tabbar>
	</view>


</template>

<script>
	import nologin from '@/components/me/nologin.vue'
	import chat from '@/subPackageA/pages/message/chat.vue'
	import {
		getUserById
	} from "@/common/API.js"
	import tabbar from "@/components/home/tabbar.vue"
	export default {
		components: {
			nologin,
			tabbar
		},
		data() {
			return {
				show: false,
				message: new Map(),
				msg: [],
				nologin: false,
			}
		},
		async onLoad(option) {
			if (!uni.getStorageSync('hasLogin')) {
				this.nologin = true;
				return
			}
			//从缓存获取列表数据
			var msgList = uni.getStorageSync('msgList');
			var tempMsg = uni.getStorageSync('tempMsg');
			this.message = new Map(msgList)
			if (tempMsg[0] instanceof Array) { //即时消息
				tempMsg = new Map(tempMsg)
				if (tempMsg.size > 0) {
					var list = [...tempMsg.values()]
					for (let i = 0; i < list.length; i++)
						await this.updateMsg(list[i], false)
				}
			} else { //未读消息
				for (let i = 0; i < tempMsg.length; i++)
					await this.updateMsg(tempMsg[i], true)
			}

			this.msg = [...this.message.values()]
			this.show = true
			uni.setStorageSync('tempMsg', [])
		},
		onShow() {
			

			
			// 如果用户状态是未登录 但是缓存中有用户登录信息
			if (this.nologin && uni.getStorageSync('hasLogin')) {
				this.nologin = false
				let cur = getCurrentPages().pop()
				cur.onPullDownRefresh()
			}
			
			this.onSocketMsg()
			var tempMsg = uni.getStorageSync('tempMsg');
			if (tempMsg.length == 0) {
				var msgList = uni.getStorageSync('msgList');
				this.message = new Map(msgList)
				this.msg = [...this.message.values()]
				this.msg.sort((a, b) => b.chatTime - a.chatTime)
			}

			let last = uni.getStorageSync('last')
			if (last) { //chat页面的最后一条记录在此更新
				let x = this.message.get(last.chatSenderId)
				if (x) {
					x.charContext = last.charContext
					x.chatTime = new Date(last.chatTime).valueOf()
					this.message.set(last.chatSenderId, x)
					this.msg = [...this.message.values()]
					this.msg.sort((a, b) => b.chatTime - a.chatTime)
					uni.setStorageSync('msgList', [...this.message])
					uni.removeStorageSync('last')
				}
			}
		},
		onPullDownRefresh() {
			this.show = false;
			this.$nextTick(() => {
				this.show = true;
				uni.stopPullDownRefresh()
			});
		},
		methods: {
			onSocketMsg() {
				
				//websocket监听函数
				uni.onSocketOpen(res => {
					console.log('在message页面--------WebSocket连接已打开!')
				})
				
				//该监听会覆盖App.vue的监听事件...
				uni.onSocketMessage(res => {
					var getMsg = JSON.parse(res.data)
					var msgList = uni.getStorageSync('msgList')
					var unreadNum = uni.getStorageSync('unreadNum')
					if (getMsg instanceof Array) { //未读消息
						let len = getMsg.length
						if (len > 0) {
							uni.setTabBarBadge({
								index: 3,
								text: '' + len
							})
							uni.showToast({
								title: '你有' + len + '条未读消息',
								icon: 'none'
							})
							uni.setStorageSync('msgList', msgList)
							uni.setStorageSync('unreadNum', unreadNum + 1)
						}
						uni.setStorageSync('unreadNum', len);
					} else { //实时消息
						var pages = getCurrentPages()
						let cur = pages[pages.length - 1];
						uni.setStorageSync('unreadNum', unreadNum + 1);
						uni.setTabBarBadge({
							index: 3,
							text: String(unreadNum + 1)
						})
						if (!cur.route.includes('message')) {
							uni.showToast({
								title: '您收到一条消息',
								icon: 'none'
							})
						}
						this.updateMsg(getMsg, true)
					}
				})
			},
			// 进入聊天页方法
			toChat(item) {
				if (item.badge > 0) {
					let unreadNum = uni.getStorageSync('unreadNum')
					if (unreadNum > item.badge) uni.setTabBarBadge({
						index: 3,
						text: String(unreadNum - item.badge)
					})
					else uni.removeTabBarBadge({
						index: 3
					})
					uni.setStorageSync('unreadNum', unreadNum - item.badge)
					item.badge = 0
					this.message.set(item.chatSenderId, item)
					uni.setStorageSync('msgList', [...this.message])
				}
				uni.navigateTo({
					url: `/subPackageA/pages/message/chat?youId=${item.chatSenderId}`
				})
			},
			del(index) {
				uni.showModal({
					content: '确认删除该消息记录?',
					success: res => {
						if (res.confirm) {
							//+删除的接口
							this.message.delete(this.msg[index].chatSenderId)
							uni.setStorageSync('msgList', [...this.message])
							this.msg.splice(index, 1)
						}
					}
				});
			},
			async updateMsg(getMsg, bool) {
				let old = this.message.get(getMsg.chatSenderId)
				if (old) {
					old.charContext = getMsg.charContext
					old.chatTime = getMsg.chatTime
					if (getMsg.badge) old.badge += getMsg.badge
					this.message.set(getMsg.chatSenderId, old)
				} else {
					await getUserById(getMsg.chatSenderId).then(rs => {
						getMsg.avatar = rs.avatar
						getMsg.openname = rs.openname
						this.message.set(getMsg.chatSenderId, getMsg)
					})
				}

				uni.setStorageSync('msgList', [...this.message])
				if (bool) this.setBadge(getMsg)
				else this.msg = [...this.message.values()]
			},
			setBadge(getMsg) {
				let old = this.message.get(getMsg.chatSenderId)
				if (old.badge) {
					old.badge++;
					this.message.set(getMsg.chatSenderId, old)
				} else {
					old.badge = 1
					this.message.set(getMsg.chatSenderId, old)
				}
				this.msg = [...this.message.values()]
				uni.setStorageSync('msgList', [...this.message])
			},
			// 格式化时间方法
			dateTime(date) {
				if (!date) return new Date()
				let old = new Date(date);
				let now = new Date();
				//获取old具体时间
				let d = old.getTime();
				let h = old.getHours();
				let m = old.getMinutes();
				let Y = old.getFullYear();
				let M = old.getMonth() + 1;
				let D = old.getDate();
				//获取now具体时间
				let nd = now.getTime();
				let nh = now.getHours();
				let nm = now.getMinutes();
				let nY = now.getFullYear();
				let nM = now.getMonth() + 1;
				let nD = now.getDate();
				//当天的时间
				if (D === nD && Y === nY && M === nM) {
					if (h < 10) {
						h = '0' + h;
					}
					if (m < 10) {
						m = '0' + m;
					}
					return h + ':' + m;
				}
				//前一天的时间
				if (D + 1 === nD && Y === nY && M === nM) {
					if (h < 10) {
						h = '0' + h;
					}
					if (m < 10) {
						m = '0' + m;
					}
					return '昨天' + ' ' + h + ':' + m;
				} else {
					//昨天之前的时间
					return Y + '/' + M + '/' + D;
				}
			}
		}
	}
</script>

<style lang="scss">
	@keyframes anim {
		0% {
			opacity: 0
		}

		;

		100% {
			opacity: 1
		}

		;
	}

	;

	.content {
		animation: anim 2s;
	}

	.message {
		width: 100%;
		height: 150rpx;
		background-color: #FFFFFF;
		border-bottom: solid 1px #e8e8e9;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.messageItem {
		width: 100%;
		height: 150rpx;
		background-color: #FFFFFF;
		border-bottom: solid 1px #e8e8e9;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-left: 29rpx;
	}

	.messageItem image {
		width: 120rpx;
		height: 120rpx;
		border-radius: 60rpx;
	}

	.itemText {
		width: 550rpx;
		height: 100rpx;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.itemTitle {
		width: 550rpx;
		height: 50rpx;
		font-size: 16px;
		background-color: #FFFFFF;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.itemTime {
			font-size: 10px;
			margin-right: 20rpx;
		}
	}

	.itemMessage {
		width: 550rpx;
		height: 40rpx;
		font-size: 12px;
		color: #808080;
		background-color: #FFFFFF;
		overflow: hidden;
		text-overflow: ellipsis;
		display: inherit;

		.text {
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
			width: 80%;
		}

		.badge {
			width: 30rpx;
			height: 30rpx;
			border-radius: 50%;
			background-color: #F95557;
			color: #fff;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-left: 54rpx;
		}
	}

	.loading {
		width: 750rpx;
		height: 300rpx;
		margin-top: 30vh;
	}
</style>
