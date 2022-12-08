<template>
	<view>
		<view class="navbar">
			<!-- 占位符，状态栏小程序撑起高度 -->
			<view :style="{height:statusBarHeight+'px'}"></view>

			<!-- nav内容区域 -->
			<view class="navbar-content" :style="{height:navBarHeight+'px'}">

				<!-- Event image -->
				<image class="back-image" src="/static/home/back.png" mode="aspectFit" v-if="!inHome" @click="back()"></image>
				<image class="event-image" src="/static/home/Event.png" mode="aspectFit" v-else></image>
				<!-- 搜索框边框 -->
				<view class="navbar-search" :style="{height:searchHeight+'rpx',width:searchWidth+'rpx'}" >

					<u--input v-model="code" border="none" type="text" :placeholder="holdWord" placeholderStyle="font-size: 28rpx"
						prefixIcon="search" prefixIconStyle="font-size: 20px;color: #808080" confirmType="search"
						@confirm="search()" @focus="toSearch()" @blur="showBtn=false">
					</u--input>
					<view v-show="showBtn">
						<div @click="search()" class="search-btn">前往</div>
					</view>

				</view>

			</view>
		</view>
		<!-- 需要添加占位符高度  状态栏高度+导航栏高度（否则下面tab会塌陷）-->
		<!-- 如果position:fixed时需要使用该段代码 -->
		<view :style="{height: statusBarHeight+navBarHeight+'px'}"></view>
	</view>
</template>

<script>
	import {getTeam,getTeamBySearch,getUserBySearch,getPostBySearch} from '@/common/API.js'
	export default {
		name: 'navbar',
		props:{
			inHome:{
				type: Boolean,
				default: false,
				
			},
			keyWord:{
				type: String,
				default(){ return '' }
			},
			fromPage:{
				type: String,
				default(){ return '' }
			}
		},
		data() {
			return {
				statusBarHeight: 20,
				/* 状态栏高度 */
				navBarHeight: 45,
				/* 导航栏高度 */
				windowWidth: 375,
				/* 窗口宽度 */
				val: '',
				/* 导航栏搜索框的值 */
				menuButtonWidth: 20, //胶囊宽度
				menuButtonHeight: 20, //胶囊高度
				menuButtonInfo: {},
				phoneScale: 2,
				/*手机的rpx比例,为750/手机宽度的比例*/
				/* px*phoneScale为rpx的长度 */
				searchWidth: 10,
				searchHeight: 10,
				code: '',
				showBtn: false,
				holdWord:'微信小程序大赛'
			};
		},
		//该函数用于获取手机型号信息
		created() {
			// 获取手机系统信息
			const info = uni.getSystemInfoSync();
			// 设置状态栏高度（H5顶部无状态栏小程序有状态栏需要撑起高度）
			this.statusBarHeight = info.statusBarHeight;
			this.windowWidth = info.windowWidth; //获取手机宽度
			this.phonephoneScale = 750 / this.windowWidth; //rpx的比例
			// 除了h5 app mp-alipay的情况下执行
			// #ifndef H5 || APP-PLUS || MP-ALIPAY
			// 获取胶囊的位置
			this.menuButtonInfo = uni.getMenuButtonBoundingClientRect();
			// console.log('胶囊位置信息:', this.menuButtonInfo);
			// (胶囊底部高度 - 状态栏的高度) + (胶囊顶部高度 - 状态栏内的高度) = 导航栏的高度
			this.navBarHeight = (this.menuButtonInfo.bottom - info.statusBarHeight) + (this.menuButtonInfo.top - info
				.statusBarHeight);

			//获取胶囊最左边的位置到屏幕右边所占的长度
			//获取胶囊的高度
			this.menuButtonWidth = (this.windowWidth - this.menuButtonInfo.left) * this.phoneScale;
			this.menuButtonHeight = (this.menuButtonInfo.bottom - this.menuButtonInfo.top) * this.phoneScale;
			// #endif
			//计算搜索栏的宽度和高度（单位:rpx）
			this.searchWidth = 540 - this.menuButtonWidth;
			this.searchHeight = this.menuButtonHeight;
		},
		mounted() {
			if(this.keyWord!=''){
				this.code=this.keyWord
			}
		},
		methods: {
			search() {
				if(this.isRealNum(this.code)){
					getTeam({teamCode:this.code}).then(rs=>{
						if(rs.length) {
							var team = rs[0]
							uni.navigateTo({url:`/subPackageA/pages/home/teamDetail?teamId=${team.teamId}&teamLeader=${team.teamLeader}`})
						} else this.$u.toast('组队码错误或已失效')
						
					})
				}else{
					//判断下一步的tab移动到哪个位置
					if(this.code=='') this.code=this.holdWord;
					var all=[];
					var team=[];
					var post=[];
					var user=[];
					var index=-1;
					const teamp={"keyword": this.code,"pageNum": 1,"pageSize": 5}
					const postp={"keyword": this.code,"pageNum": 1,"pageSize": 5}
					const userp={"userId": uni.getStorageSync('userId'),"keyword": this.code,"pageNum": 1,"pageSize": 8}
					const p1={"keyword": this.code,"pageNum": 1,"pageSize": 3}
					const p2={"keyword": this.code,"pageNum": 1,"pageSize": 2}
					getPostBySearch(p1).then(res =>{
						  all=res;
						  getTeamBySearch(p2).then(rs => {
								all=all.concat(res);
								if(all.length>0){
									index=0;
									if(this.fromPage=='searchDetail'){
										uni.redirectTo({url:`/subPackageA/pages/home/searchDetail?key=${this.code}&index=${index}`})
									}else{
										uni.navigateTo({url:`/subPackageA/pages/home/searchDetail?key=${this.code}&index=${index}`})
									}
								}else{
									getTeamBySearch(teamp).then(rs=>{
										team = rs;
										getPostBySearch(postp).then(rs=>{
											post = rs;
											getUserBySearch(userp).then(rs=>{
												user = rs;
												if(team.length>0){
													index=1;
												}else if(post.length>0){
													index=2;
												}else if(user.length>0){
													index=3;
												}else{
													index=0;
												}
												if(this.fromPage=='searchDetail'){	
													uni.redirectTo({url:`/subPackageA/pages/home/searchDetail?key=${this.code}&index=${index}`})
												}else{
													uni.navigateTo({url:`/subPackageA/pages/home/searchDetail?key=${this.code}&index=${index}`})
												}
											},err=>{console.log(err)})
										},err=>{console.log(err)})
									},err=>{console.log(err)})
								}
						  },err=>{console.log(err)})
					},err=>{console.log(err)})	
				}
			},
			back(){
				uni.navigateBack({
					delta: 1
				});
			},
			isRealNum(val){
				if(val === "" || val ==null) return false
				if(!isNaN(val)) return true
				else return false
			},
			toSearch(){
				if(this.inHome){
					uni.navigateTo({
						url:'/subPackageA/pages/home/searchHub'
					})
				}else{
					this.showBtn=true
				}
			}
		}
	}
</script>

<style lang="scss">
	@keyframes anim { 
		0% {opacity: 0};
		100%{opacity:1};
	};
	.navbar {
		position: fixed;
		z-index: 99;
		width: 100%;
		background-color: #FFFFFF;

		.navbar-content {
			position: relative;
			overflow: hidden;
			margin: 10rpx 0 0 0;
			height: 180rpx;
			width: 100%;
			box-sizing: border-box;

			.words {
				display: inline-block;
				font-weight: 700;
				font-size: 35rpx;
				margin-right: 20rpx;
			}

			.event-image {
				position: absolute;
				height:80rpx;
				width: 150rpx;
				left: 10rpx;
			}
			.back-image {
				position: absolute;
				height:40rpx;
				width: 150rpx;
				left: 10rpx;
				top: 10rpx;
			}

			.navbar-search {
				position: absolute;
				left: 170rpx;
				top: 0;
				padding: 0 20rpx;
				width: 50%;
				border-radius: 40rpx;
				background-color: #f6f6f6;
				display: flex;
				align-items: center;
				justify-content: center;

				.search-btn {
					font-size: 12px;
					color: #6b6b6bee;
					background: #fff;
					border-radius: 61rpx;
					width: 79rpx;
					height: 49rpx;
					display: flex;
					justify-content: center;
					align-items: center;
					animation: anim 0.5s;
				}
			}

		}
	}
</style>
