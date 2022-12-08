<template>
	<view class="outer" v-show="show">
		<!-- 消息记录滚动框 -->
		<scroll-view class="chatScroll" scroll-y="true" :scroll-into-view="scrollToView" :scroll-with-animation="scrollAni" @scrolltoupper="loadHistory" upper-threshold="10" :style="{'height':scrollHeight }">
			<view class="chatScrollBox">
				<!-- 单条消息块 -->
				<view class="chatMain" v-for="(item,index) in chatMessage" :id="'msg'+item.chatId" :key="item.chatId">
					<!-- 消息时间 -->
					<view class="chatTime" v-if="index==0">{{chatTime(item.chatTime)}}</view>
					<view class="chatTime" v-else-if="spaceTime(chatMessage[index-1].chatTime,item.chatTime)=='yes'">{{chatTime(item.chatTime)}}</view>
					<!-- 对方消息 -->
					<view class="chatOtherItem" v-if="item.chatSenderId==you.userid">
						<!-- 对方用户头像 -->
						<image @click="goUser(you.userid)" :src="you.avatar" class="chatPortrait"></image>
						<!-- 对方文字消息 -->
						<view class="chatOtherMessage">
							<view class="chatOtherText"><text user-select=true>{{item.charContext}}</text></view>
						</view>
						<!-- 对方图片消息 -->
						<!-- <view class="chatOtherPictuer" v-if="item.type==1">
							<image :src="item.message" mode="widthFix" style="max-width: 450rpx;border-radius: 10rpx;" @tap="previewImage()"></image>
						</view> -->
					</view>
					<!-- 本人消息 -->
					<view class="chatMineItem" v-else>
						<!-- 本人头像 -->
						<image @click="goUser(me.userid)" :src="me.avatar" class="chatPortrait"></image>
						<!-- 本人文字消息 -->
						<view class="chatMineMessage">
							<view class="chatMineText"><text user-select=true>{{item.charContext}}</text></view>
						</view>
						<!-- 本人图片消息 -->
						<!-- <view class="chatMinePictuer" v-if="item.type==1">
							<image :src="item.message" mode="widthFix" style="max-width: 360rpx;border-radius: 10rpx;" @tap="previewImage()"></image>
						</view> -->
					</view>
				</view>
			</view>
		</scroll-view>

		
		
			<!-- 底部发送栏 -->
		<sendInput ref="sendInput" v-on:send_chat="submit()" v-on:setScrollHeight="setScrollHeight()" :holdWord="holdWord" inPage="chat"></sendInput>
		</view>
</template>

<script>
	import {getUserById,getChatTwo,pushChat} from "@/common/API.js"
	import sendInput from "../../components/common/sendInput.vue"
	export default {
		components: { sendInput },
		data() {
			return {
				show: false,
				holdWord:"写私信...",
				chatMessage: [],
				imageMessage: [],//图片消息记录
				scrollToView: '',//scroll-view要滚动到的元素id
				scrollHeight: '',//scroll-view窗口高度
				scrollAni: false,
				aniTime: '.4s',
				you:{},
				me:{},
				page:0,
				topLoading: false
			}
		},
		onLoad(option) {
			uni.showNavigationBarLoading()
			this.onSocketMsg();
			this.me = uni.getStorageSync('userInfo')
			getUserById(option.youId).then(rs => {this.you = rs;uni.setNavigationBarTitle({title:this.you.openname})})
			getChatTwo(option.youId,this.me.userid,this.page).then(rs => {
				this.chatMessage = rs.reverse()
				this.goBottom();
				this.$nextTick(() => {
					this.show = true;
					this.scrollAni = true;
					uni.hideNavigationBarLoading()
				});
			})

			const windowHeight = this.getWindowHeight()
			const windowWidth = this.getWindowWidth()
			const scrollHeight = windowHeight * (750 / windowWidth) - 115;
			scrollHeight: '';
			this.scrollHeight = scrollHeight + 'rpx';
			// console.log(this.scrollHeight);
			// this.orderImage();
			// console.log(this.imageMessage);
			
			// this.goBottom();
			// this.scrollAni = true;
			// console.log('yes ok!');
		},
		beforeDestroy() {
			let last = this.chatMessage.pop()
			if(last){
				last.chatSenderId = this.you.userid
				uni.setStorageSync('last',last)
			}
			
		},
		methods: {
			onSocketMsg() {
				//该监听会覆盖message的监听事件...
				uni.onSocketMessage(res => {
					var getMsg = JSON.parse(res.data)
					this.chatMessage.push({
						chatId: this.chatMessage.length,
						chatSenderId: this.you.userid,
						charContext: getMsg.charContext,
						chatTime: new Date(),
					})
					this.goBottom()
				})
			},
			//触发滑动到顶部(加载历史信息记录)
			loadHistory(e){
				if(this.topLoading || this.chatMessage.length==0) return
				uni.showNavigationBarLoading()
				this.topLoading = true
				this.scrollAni = false; //关闭滑动动画
				this.page++;
				let Viewid = this.chatMessage[0].chatId;//记住第一个信息ID
				getChatTwo(this.you.userid,this.me.userid,this.page).then(rs => {
					rs.reverse().forEach(item => this.chatMessage.unshift(item))
					this.$nextTick(() => {
						this.scrollToView = 'msg'+Viewid;//跳转上次的第一行信息位置
						this.$nextTick(() => {
							this.scrollAni = true;//恢复滚动动画
						});
					});
					setTimeout(()=>{
						this.topLoading = false
						uni.hideNavigationBarLoading()
					},800)
				})
			},
			goUser(userId) {
				uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+userId})
			},
			// 发送消息方法
			submit(textMsg) {
				pushChat(this.me.userid,this.you.userid,textMsg)
				let getMsg = {
					chatId: this.chatMessage.length,
					chatSenderId: this.me.userid,
					charContext: textMsg,
					chatTime: new Date(),
				}
				this.chatMessage.push(getMsg)
				// console.log(this.chatMessage.length)
				this.goBottom();
			},
			// 改变聊天框高度
			async setScrollHeight(res,keyHeight) {
					const windowHeight = this.getWindowHeight()
					const windowWidth = this.getWindowWidth()
					var scrollHeight=0;
					if(res=="show"){
						scrollHeight: '';
						scrollHeight = windowHeight * (750 / windowWidth) - 630;
						
						
					}
					else if(res=="hide"){
						scrollHeight = windowHeight * (750 / windowWidth) - keyHeight -135;
						
					}
					else{
						scrollHeight = windowHeight * (750 / windowWidth) -115;
						scrollHeight: '';
					}
					console.log("最终："+scrollHeight)
					this.scrollHeight = scrollHeight + 'rpx';
					this.goBottom();
			},
			// 滑动到底部方法
			goBottom() {
				this.scrollToView = '';
				this.$nextTick(function() {
					let len = this.chatMessage.length - 1;
					if(len>=0){
						this.scrollToView = 'msg' + this.chatMessage[len].chatId;
					}
					
				})

			},
			
			// 获取屏幕高度方法
			getWindowHeight() {
				const res = uni.getSystemInfoSync();
				return res.windowHeight;
			},
			
			// 获取屏幕宽度方法
			getWindowWidth() {
				const res = uni.getSystemInfoSync();
				return res.windowWidth;
			},
			
			// 预览图片方法
			previewImage() {
				uni.previewImage({
					urls: this.imageMessage,
					longPressActions: {
						itemList: ['发送给朋友', '保存图片', '收藏'],
						success: function(data) {
							console.log('选中了第' + (data.tapIndex + 1) + '个按钮,第' + (data.index + 1) + '张图片');
						},
						fail: function(err) {
							console.log(err.errMsg);
						}
					}
				});
			},
			
			// 将消息记录中图片消息存储到图片数组方法
			orderImage() {
				for (let i = 0; i < this.chatMessage.length; i++) {
					if (this.chatMessage[i].type == 1) {
						this.imageMessage.unshift(this.chatMessage[i].message);
					}
				}

			},
			
			// 发送图片方法
			sendImage() {
				uni.chooseImage({
					count: 9, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: (res) => {
						const filePaths = res.tempFilePaths;
						for (let i = 0; i < filePaths.length; i++) {
							this.submit(filePaths[i], 1);
							this.imageMessage.push(filePaths[i]);
						}
						console.log(JSON.stringify(res.tempFilePaths));
			  }
				});
			},
			
			
			// 格式化聊天时间方法
			chatTime(date) {
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
				} else if (Y === nY) {
					//今年时间
					return M + '月' + D + '日' + ' ' + h + ':' + m;
				} else {
					return Y + '年' + M + '月' + D + '日' + ' ' + h + ':' + m;
				}
			},
			
			// 判断时间间隔方法
			spaceTime(old, now) {
				old = new Date(old);
				now = new Date(now);
				var told = old.getTime();
				var tnow = now.getTime();
				if (tnow > (told + 1000 * 60 * 3)) {
					return "yes";
				} else {
					return "no";
				} 
			},
		}
	}
</script>

<style lang="scss">
	.outer {
		width: 100%;
		height: 100%;

			.chatScroll {
				width: 100%;
				z-index: 1;
			}
	}

	.chatScrollBox {
		padding-top: 50rpx;
		padding-bottom: 20rpx;

	}

	.chatTime {
		display: inline-block;
		color: #000;
		font-size: 30rpx;
		padding: 0 30rpx;
		background-color: rgba(118, 116, 112, 0.3);
		border-radius: 10rpx;

	}

	.chatMain {
		width: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.chatPortrait {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
	}

	.chatOtherItem {
		width: 715rpx;
		padding: 20rpx 20rpx;
		display: flex;
	}

	.chatMineItem {
		width: 715rpx;
		padding: 20rpx 20rpx;
		display: flex;
		flex-direction: row-reverse;
	}

	.chatOtherMessage {
		display: inline-block;
		margin-top: 20rpx;
		padding: 20rpx 20rpx;
		margin-left: 10rpx;
		border-radius: 10rpx;
		border-top-left-radius: 0;
		background-color: #f1f2f3;
		font-size: 35rpx;

		.chatOtherText {
			height: auto;
			max-width: 490rpx;
			word-wrap: break-word;
			word-break: break-all;
		}
	}

	.chatMineMessage {
		height: auto;
		display: inline-block;
		margin-top: 20rpx;
		padding: 20rpx 20rpx;
		margin-right: 10rpx;
		border-radius: 10rpx;
		border-top-right-radius: 0;
		background-color: #ec4141;
		color: #FFFFFF;
		font-size: 35rpx;

		.chatMineText {
			height: auto;
			max-width: 490rpx;
			word-wrap: break-word;
			word-break: break-all;
		}

	}

	.chatMinePictuer {
		max-width: 360rpx;
		margin-right: 15rpx;
		border-radius: 10rpx;
		padding-top: 20rpx;
	}

	.chatOtherPictuer {
		max-width: 360rpx;
		margin-left: 15rpx;
		border-radius: 10rpx;
		padding-top: 20rpx;
	}

	.displayNone {
		display: none;
	}

	.push {
		z-index: 66;
	}

</style>
