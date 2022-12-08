<template>
	<!-- 底部发送栏 -->
	<view class="bottomAll" :style="{'bottom':keyHeight+'rpx' ,'transition': 'bottom '+ aniTime }">
		<view class="submit" id="submit">
      <view class="submitBox">
        <!-- 输入框 -->
        <view class="submitScroll" scroll-y="true" >
          <textarea :auto-height="true" v-model="textMsg" :placeholder="holdWord" 
            cursor-spacing="30rpx" :show-confirm-bar="false" :focus="isFocus"  @focus="hideEmoji" @blur="refresh"
            :adjust-position="false" :hold-keyboard="false" />
        </view>
        <!-- 选择表情按钮 -->
        <view class="utilBox">
          <view class="submitEmoji" @click.stop="showEmoji()"></view>
          <!-- 选择图片按钮 -->
          <!-- <image src="/static/message/icon/image.png" class="submitPicture" v-if="textMsg==''" @click="sendImage()"></image> -->
          <!-- 发送按钮 -->
          <image src="../../static/message/icon/send.png" class="submitPicture" @click="submit(textMsg,0)"></image>
        </view>
      </view>

      </view>
      <!-- 表情展示块 -->
      <view class="add" :style="{display:ishide}">
        <scroll-view class="emoji" scroll-y="true">
          <view class="emojiLine" v-for="(line,i) in emoji" :key="i">
            <view class="emojiItem" v-for="(item,index) in line" :key="index" @click="getEmoji(item)">{{item}}</view>
          </view>
        </scroll-view>
      </view>
      <!-- 底部安全距离块 -->
      <view class="bottomBar" :style="{display:ishide}"></view>
		</view>
</template>

<script>
	export default{
		props:{
			inPage:{
				type: String,
				default(){ return '' }
			},
			holdWord:{
				type: String,
				default(){ return '' }
			}
		},
		data() {
			return {
				show: false,
				textMsg: '',//输入框文本信息
				ishide: "none",//表情展示块状态变量
				aniTime: '.4s',
				emoji: [//表情数组
					['😀', '😁', '😂', '🤣', '😃', '😄', '😅', '😆'],
					['😉', '😊', '😎', '😍', '😘', '😗', '😙', '😚'],
					['☺️', '🙂', '🤗', '🤔', '😐', '😑', '😶', '🙄'],
					['😏', '😣', '😥', '😮', '🤐', '😯', '😪', '😫'],
					['😴', '😌', '😛', '😜', '😝', '🤤', '😒', '😓'],
					['😔', '😕', '🙃', '🤑', '😲', '😞', '🙁', '😖'],
					['😟', '😤', '😢', '😭', '😦', '😧', '😨', '😩'],
					['😬', '😰', '😱', '😳', '😵', '😡', '😠', '😷'],
					['🤒', '🤕', '🤢', '🤧', '😇', '🤠', '🤡', '🤥'],
					['🤓', '😈', '👿', '👹', '👺', '💀', '👻', '👽'],
					['🤖', '💩', '😺', '😸', '😹', '😻', '😼', '😽'],
					['👫', '👬', '👭', '👂', '👃', '👣', '👀', '👁'],
					['👅', '👄', '💋', '👓', '🕶', '👔', '👕', '👖'],
					['👗', '👘', '👙', '👚', '👛', '👜', '👝', '👑'],
					['🌂', '👞', '👟', '👠', '👡', '👢', '👒', '💼']
				],
				keyHeight:'',
				isFocus:false,
			}
		},
		mounted() {
			uni.onKeyboardHeightChange(res => {
				// console.log("软键盘高度监听"+res.height);
				const windowWidth = this.getWindowWidth()
				this.keyHeight=res.height * (750 / windowWidth);
				// console.log("软键盘高度为"+this.keyHeight);
				if(res.height==0) this.aniTime='0s'
				else this.aniTime='.4s'
				//监听软键盘的高度 
				//当点击软键盘自带的收起按钮的时候也就是会隐藏软键盘 这时候监听到的软键盘的高度就是0 、
				//让输入框取消焦点 这时候再去输入内容的时候 输入框就会弹起
				uni.$u.throttle(()=>{ this.keyHeight=res.height * (750 / windowWidth) },20)
				console.log(`[info] 软键盘高度变化:${JSON.stringify(res)}`)
			});
			if(this.ishide=="block")
			{
				uni.hideKeyboard();
				// console.log("ishide:"+this.ishide);
			}
			uni.createSelectorQuery().selectViewport().scrollOffset(res => {
			  // console.log("竖直滚动位置" + res.scrollTop);
			}).exec();
			
			let view = uni.createSelectorQuery().in(this).select(".bottomAll");
			
			view.fields({
			  size: true,
			  scrollOffset: true
			}, data => {
			  // console.log("得到节点信息" + JSON.stringify(data));
			  // console.log("节点的高为" + data.height);
			}).exec();
			 console.log("holdWord:"+this.holdWord)
			 console.log("isFocus:"+this.isFocus)
			 console.log("inPage:"+this.inPage)
			
		},
		methods: {
			// 发送消息方法
			submit() {
				if(this.textMsg=="") { uni.$u.toast('请输入内容'); return }
				else if(!uni.getStorageSync("hasLogin")){
          uni.$u.toast('请在登陆后进行操作');
          setTimeout(() => { uni.navigateTo({ url: '/subPackageA/pages/me/login' }) }, 1000)
        }
				else if(this.inPage=="detail")
				{
					this.$emit("send_detail",this.textMsg);
					
				}
				else if(this.inPage=="replyDetail")
				{
					this.$emit("send_replyDetail",this.textMsg);
				}
				else if(this.inPage=="chat")
				{
					this.$emit("send_chat",this.textMsg);
					this.$emit("goBottom")
				}
				this.hideEmoji();
				this.isFocus=false;
				this.keyHeight=0;
				this.textMsg = ''
				// console.log(this.chatMessage.length)
			},
			// 展示表情方法
			async showEmoji() {
				uni.hideKeyboard()
				if (this.ishide == "none") {
					await setTimeout(()=>{
						this.ishide = "block";
					},100)
					if(this.inPage=="chat"){
						this.$emit("setScrollHeight","show")
						console.log("showEmoji")
					}
						
				} else {
					this.hideEmoji()
				}
				
			},
			
			// 收起表情方法
			hideEmoji() {
				this.ishide = "none";
				uni.onKeyboardHeightChange(res => {
					// console.log("软键盘高度监听"+res.height);
					const windowWidth = this.getWindowWidth()
					this.keyHeight=res.height * (750 / windowWidth);
					// console.log("软键盘高度为"+this.keyHeight);
					if(res.height==0) this.aniTime='0s'
					else this.aniTime='.4s'
					//监听软键盘的高度 
					//当点击软键盘自带的收起按钮的时候也就是会隐藏软键盘 这时候监听到的软键盘的高度就是0 、
					//让输入框取消焦点 这时候再去输入内容的时候 输入框就会弹起
					uni.$u.throttle(()=>{ this.keyHeight=res.height * (750 / windowWidth) },20)
					console.log(`[info] 软键盘高度变化:${JSON.stringify(res)}`)
				});
				if(this.inPage=="chat"){
					this.$emit("setScrollHeight","hide",this.keyHeight)
					console.log("hideEmoji")
				}
					
				
			},
			refresh(){
				if(this.inPage=="chat")
				{
					console.log(this.ishide)
					if(this.ishide=="block"){
						this.$emit("setScrollHeight","show")
						console.log("refreshShow")
					}
					else{
						this.$emit("setScrollHeight")
						console.log("refresh")
					}
				}else{
					if(this.ishide=="block"){
						this.isFocus=false
					}else{
						this.hideEmoji()
						this.isFocus=false
					}
					
				}
				
				
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
			
			// 获取表情方法
			getEmoji(e) {
				this.textMsg += e;
			},
		}
	}
</script>

<style lang="scss">
	.bottomAll {
		width: 100%;
		z-index: 20;
		position: fixed;
		bottom: 0rpx;
	}
	
	.submit {
		width: 100%;
		max-height: 200rpx;
		min-height: 75rpx;
		padding-top: 20rpx;
		padding-bottom: 20rpx;
		background-color: #f8f8f8;
		display: flex;
		justify-content: center;
		align-items: center;
		border-top: solid 1rpx #d6d5d4;
	
	}
	
	.submitBox {
		width: 88%;
		max-height: 160rpx;
		min-height: 75rpx;
	
		background-color: #f8f8f8;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-sizing: border-box;
	
		.submitScroll {
			width: 480rpx;
			max-height: 160rpx;
			min-height: 80rpx;
			border-radius: 8rpx;
			background-color: #FFFFFF;
			display: flex;
			align-items: center;
			textarea {
				width: 440rpx;
				min-height: 30rpx;
				max-height: 160rpx;
				line-height: 30rpx;
				background-color: #FFFFFF;
				margin-left: 20rpx;
			}
		}
		.utilBox{
			width: 150rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
		}
	
		.submitEmoji {
			background: url(../../static/message/icon/表情.png);
			background-size: contain;
			background-repeat: no-repeat;
			width: 60rpx;
			height: 60rpx;
			margin-right: 20rpx;
		}
	
		.submitPicture {
			width: 55rpx;
			height: 55rpx;
		}
	}
	
	.add {
		width: 100%;
		height: 450rpx;
		background-color: #FFFFFF;
		display: none;
	
		top: 100%;
		.emoji {
			height: 450rpx;
			padding-bottom: 20rpx;
		}
	}
	
	.bottomBar {
		width: 100%;
		height: 65rpx;
		background-color: #FFFFFF;
		border-top: solid 1rpx #d6d5d4;
		display: none;
	
		top: 100%;
	}
	.emojiLine {
		display: flex;
		justify-content: space-between;
		padding: 10rpx 20rpx;
	}
</style>