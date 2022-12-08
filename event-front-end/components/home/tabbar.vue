<template>
	<view id="tab" class="tabbar">
    <u-tabbar
			:value="value"
			activeColor="#000F"
			inactiveColor="#BBBBBB"
			:fixed="true"
			:placeholder="true"
			:safeAreaInsetBottom="true"
		>
			<u-tabbar-item text="首页" class="item" @click="click0" >
				<image
					class="tabbarIcon"
					slot="active-icon"
					src="../../static/tabBar-icon/home.png"
				></image>
				<image
					class="tabbarIcon"
					slot="inactive-icon"
					src="../../static/tabBar-icon/home_not.png"
				></image>
			</u-tabbar-item>
			<u-tabbar-item text="人脉"  @click="click1" >
				<image
					class="tabbarIcon"
					slot="active-icon"
					src="../../static/tabBar-icon/friend-fill.jpg"
				></image>
				<image
					class="tabbarIcon"
					slot="inactive-icon"
					src="../../static/tabBar-icon/friend.jpg"
				></image>
			</u-tabbar-item>
			<u-tabbar-item customStyle="midButton" text="发布" @click="click2" >
				<image
					class="tabbarIcon"
					slot="active-icon"
					src="../../static/tabBar-icon/plus-circle-fill.png"
				></image>
				<image
					class="tabbarIcon"
					slot="inactive-icon"
					src="../../static/tabBar-icon/plus-circle.png"
				></image>
			</u-tabbar-item>
			<u-tabbar-item text="消息"  @click="click3" >
				<image
					class="tabbarIcon"
					slot="active-icon"
					src="../../static/tabBar-icon/office.png"
				></image>
				<image
					class="tabbarIcon"
					slot="inactive-icon"
					src="../../static/tabBar-icon/office_not.png"
				></image>
			</u-tabbar-item>
			<u-tabbar-item text="我的"  @click="click4" >
				<image
					class="tabbarIcon"
					slot="active-icon"
					src="../../static/tabBar-icon/mine.png"
				></image>
				<image
					class="tabbarIcon"
					slot="inactive-icon"
					src="../../static/tabBar-icon/mine_not.png"
				></image>
			</u-tabbar-item>
		</u-tabbar>
		<u-popup :show="showPopup" :round="14" @close="close" :safeAreaInsetBottom="false" duration="300">
		    <view class="option">
				<view class="optionContainer">
					<view class="addTeam" @click="toAddTeam">
						<text class="mainTitleLeft">发布组队</text>
						<text class="infoTextLeft">寻找志同道合的队友</text>
					</view>
					<view class="addPostIcon">
					</view>
					<view class="addPost" @click="toAddPost">
						<text class="mainTitleRight">发布想法</text>
						<text class="infoTextRight">说说自己的想法体会</text>
					</view>
				</view>
		    </view>
		
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				value: this.$store.state.tabbarValue,
				showPopup: false
			}
		},
    mounted() {
      // 判断当前页面
      // console.log(`[info] tabbar mounted`)
      let currentPageStr = uni.$u.page()
      if(currentPageStr == '/pages/home/home') this.value = 0
      if(currentPageStr == '/pages/contact/contact') this.value = 1
      // if(currentPageStr == '/pages/post/post') this.value = 1
      if(currentPageStr == '/pages/message/message') this.value = 3
      if(currentPageStr == '/pages/me/me') this.value = 4
    },
		methods:{
			click0(name) {
				this.$store.commit('refreshTabbar',name)
				uni.switchTab({ url: '/pages/home/home' })
				const query = uni.createSelectorQuery().in(this);
				query.select('#tab').boundingClientRect(data => {
					// console.log(data.height)
				}).exec();
			},
			click1(name) {
				console.log(`[info] click1`)
				this.$store.commit('refreshTabbar',name)
				// uni.switchTab({ url: '/pages/post/post' })	
				uni.switchTab({ url: '/pages/contact/contact' })
			},
			click2(name) {
				if(!uni.getStorageSync('hasLogin')){
				  uni.showToast({title:'请在登录后进行操作',icon:'none'})
				  return
				}
				if(this.showPopup==false) this.showPopup=true;
				else this.showPopup=false;
        setTimeout(() => {
          // 判断是否完善个人信息
          if(this.$common.calPercentage()[0] < 60) return
        }, 500);
			},
			click3(name) {
				this.$store.commit('refreshTabbar',name)
				uni.switchTab({ url: '/pages/message/message' })
			},
			click4(name) {
				this.$store.commit('refreshTabbar',name)
				uni.switchTab({ url: '/pages/me/me' })
			},
			close() {
        this.showPopup = false
			},
			toAddTeam(){
				this.close()
				uni.navigateTo({ url: '../../subPackageA/pages/team/teamSend' })
			},
			toAddPost(){
				this.close()
				uni.navigateTo({ url:"../../subPackageA/pages/post/create" })
			}
		}
	}
</script>

<style lang="scss">
	.tabbarIcon{
		width: 26px;
		height: 26px;
	}
	.midButton{
		width: 115rpx;
		height: 115rpx;
		background-color: #FFFFFF;
		border-radius: 50%;
		margin-bottom: 30rpx;
		padding: 6rpx;
		border: 2rpx solid #dadbde;
	}
	.option{
		width: 100%;
		height: 400rpx;
		border-radius: 14px;
		//background: linear-gradient(#ffffff, #bc696b);
		display: flex;
		justify-content: center;
		align-items: center;
		background-image: url("../../static/tabBar-icon/post.png");
		background-size: contain;
		background-repeat: no-repeat;
		.optionContainer{
			width: 650rpx;
			height: 260rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			align-items: center;
			
			.addTeam{
				width: 220rpx;
				height: 120rpx;
				background-color: #c35556;
				border-radius: 10rpx;
				display: flex;
				flex-direction: column;
				justify-content: center;
				padding: 15rpx;
				justify-content: center;
				margin-left: 100rpx;
				margin-top: -8rpx;
			}
			.addPostIcon{
				width: 150rpx;
				height: 150rpx;
				background-image: url("../../static/tabBar-icon/idea.png");
				background-size: contain;
				background-repeat: no-repeat;
				margin-left: 500rpx;
				
				position: absolute;
			}
			.addPost{
				width: 220rpx;
				height: 120rpx;
				background-color: #c35556;
				border-radius: 10rpx;
				display: flex;
				flex-direction: column;
				justify-content: center;
				padding: 15rpx;
				justify-content: center;
				margin-left: 420rpx;
				margin-top: 40rpx;
				
			}
			.mainTitleLeft{
				font-size: 30rpx;
				font-weight: bold;
				color: #FFFFFF;
				margin-bottom: 5rpx;
			}
			.mainTitleRight{
				font-size: 30rpx;
				font-weight: bold;
				color: #FFFFFF;
				margin-bottom: 5rpx;
			}
			.infoTextLeft{
				font-size: 20rpx;
				color: #FFFFFF;
				
			}
			.infoTextRight{
				font-size: 20rpx;
				color: #FFFFFF;
				
			}
		}
		
	}
</style>
