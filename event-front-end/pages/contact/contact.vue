<template>
	<view >
		<template v-if="nologin">
			<nologin></nologin>
		</template>
		<template v-else>
		<view class="seg" id="top">
			<segmenter ref="seg" :tabData="tabs" :activeIndex="activeIndex" :config="config"
				@tabClick="tabClick($event, '默认设置')" />
		</view>
		<view>
			<view class="contentMain" >
				<view class="flex-col content" :style="{height:swiperHeight+'rpx'}">
					<swiper class="swiper-box" :current="activeIndex" @change="updateIndex" >
						<!-- 人脉 -->
						<swiper-item class="swiper-item">
							<view class="all">
								<scroll-view scroll-y="true" class="all" refresher-enabled enable-back-to-top lower-threshold=250 @scrolltolower="reachBottom()" :refresher-triggered="refresh" @refresherrefresh="pullDownRefresh()">
									<view>
										<view class="top">
											<view class="picture"></view>
											<view class="up">
												<!-- <image src="../../static/contact/2815378510386282.png" style="picture"></image> -->
												<view class="title">
													构建属于你的人脉圈
												</view>
												<view class="down">
													<view class="info">
														<view class="item" @click="toFollow()">
															<view class="itemInfo">
																<u-icon name="account-fill" color="#dd524d" size="20"></u-icon>
																{{followNum}}
															</view>
															<view style="font-size: 24rpx;color: #888;">
																我感兴趣
															</view>
														</view>
														<view class="item" @click="toFans()">
															<view class="itemInfo">
																<u-icon name="heart-fill" color="#dd524d" size="20"></u-icon>
																{{fansNum}}
															</view>
															<view style="font-size: 24rpx;color: #888;">
																对我感兴趣
															</view>
															
						 								</view>
													</view>
												</view>
											</view>
										</view>
										<view class="tag">
											<view class="tagItem">
												<u-tag  text="为你推荐" bgColor="#dd524d" borderColor="#dd524d" ></u-tag>
												<view class="icon">
													
												</view>
											</view>
											
										</view>
										<view class="bottom">
											<users ref="more" fromPage="contact"></users>
										</view>
									</view>
								</scroll-view>
							</view>
							
							
						</swiper-item>
						<!-- 动态-->
						<swiper-item class="swiper-item">
							<posts fromPage="contact"></posts>
						</swiper-item>
					</swiper>
				</view>
			</view>
		</view>
		</template>
		<tabbar></tabbar>
	</view>
</template>

<script>
  import tabbar from "@/components/home/tabbar.vue"
  import users from '@/components/contact/users.vue'
  import segmenter from "@/components/common/segmenter.vue"
  import team from "@/components/home/team.vue"
  import posts from "@/components/post/posts.vue"
  import nologin from '@/components/me/nologin.vue'
  import {getFollowNum,getFansNum,getFollow,getFollowPost,recommendUser} from '@/common/API.js'
	export default {
    components:{ tabbar,users,segmenter,team,posts,nologin },
		data() {
			return {
				swiperHeight: 0,
				topHeight:0,
				bottomHeight:0,
				// 分段器配置
				activeIndex: 0,
				followNum:'-',
				fansNum:'-',
				activeIndex: 0,
				tabs: [
					{state: -1,name: '人脉'},
					{state: 1,name: '动态'},
				],
				config: {
					key: 'name', // 要显示的key
					fontSize: 30, // 字体大小 rpx
					color: '#7d7d7f', // 字体颜色
					activeColor: '#242424', // 激活字体颜色
					itemWidth: 0, // item宽度 0为自动
					underLinePadding: 10, // 下划线左右边距，文字宽度加边距，如果设置了itemWidth则为itemWidth加边距 rpx
					underLineWidth: 80, // 下划线宽度 rpx  注意：设置了此值 underLinePadding 失效
					underLineHeight: 0, // 下划线高度 rpx
					underLineColor: 'linear-gradient(270deg, #feaeb1 0%, #f6585b 100%)' // 下划线颜色
				},
				refresh:false,
				nologin: false,
			};
		},
    onLoad() {
      if (!uni.getStorageSync('hasLogin')) {
        this.nologin = true;
        return
      }
      this.initData()
    },
    onShow() {
      // 冗余判断，用于防止在人脉板块登陆时产生的显示问题
      if (!uni.getStorageSync('hasLogin')) {
        this.nologin = true
      }else{
        this.nologin = false
        this.$disp.fire(`event.fresh_contact`)
        this.initData()
      }
    },
    methods:{
      initData(){
        getFollowNum(uni.getStorageSync('userId')).then(rs => { this.followNum = rs;},err=>{console.log(`[error] ${err}`)})
        getFansNum(uni.getStorageSync('userId')).then( rs => { this.fansNum = rs },err=>{console.log(`[error] ${err}`)})
        var windowHeight = uni.getSystemInfoSync().windowHeight;
        var windowWidth = uni.getSystemInfoSync().windowWidth;
        var windowHeight = uni.getSystemInfoSync().windowHeight;
        var windowWidth = uni.getSystemInfoSync().windowWidth;
        var windowHeight = uni.getSystemInfoSync().windowHeight;
        var windowWidth = uni.getSystemInfoSync().windowWidth;
        const query = uni.createSelectorQuery().in(this);
        query.select('#top').boundingClientRect(data => {
          console.log(data)
          this.swiperHeight = parseInt(((windowHeight - 100 - data.height + 1) * (750 / windowWidth))); 
        }).exec();
      },
      toFollow(){
        uni.navigateTo({
          url:'/subPackageA/pages/contact/myFollows'
        })
      },
      toFans(){
        uni.navigateTo({
          url:'/subPackageA/pages/contact/myFans'
        })
      },
      tabClick(index, name) {
        this.activeIndex = index
      },
      updateIndex(e) {
        this.activeIndex = e.detail.current
        this.$refs.seg.tabClick(this.activeIndex)
      },
      reachBottom() {
        this.$refs.more.reachBottom();
      },
      async pullDownRefresh(){
        this.refresh=false;
        this.refresh=true;
        getFollowNum(uni.getStorageSync('userId')).then(rs => { this.followNum = rs;},err=>{console.log(err)})
        getFansNum(uni.getStorageSync('userId')).then( rs => { this.fansNum = rs },err=>{console.log(err)})
        this.$refs.more.pullDownRefresh().then(res=>{
          this.refresh=false
        });
      },

    }
  }
</script>

<style lang="scss">
	.contentMain{
		display: flex;
		flex-direction: column;
		.all{
			height: 100%;
		}
	}
	.content {
		padding-top: 0rpx;
		
		.swiper-box {
			flex: 1;
			
			.swiper-item {
				height: 100%;
			}
		}
			
	}
	.seg {
		white-space: initial;
		width: 300rpx;
		margin-top: 80rpx;
		margin-left: 20rpx;
	}
	.top{
		width: 100%;
		height: 500rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		.picture{
			width: 180rpx;
			height: 180rpx;
			background-image: url('@/static/contact/friend.png');
			background-size: contain;
			background-repeat: no-repeat;
			position: absolute;
			right: 50rpx;
			top: 105rpx;
		}
		.up{
			width: 690rpx;
			height: 300rpx;
			background-color: #dd524d;
			border-radius: 30rpx;
			margin-bottom: 140rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			
			.title{
				display: flex;
				align-items: center;
				font-size: 50rpx;
				font-weight: bold;
				color: #FFFFFF;
				margin-top: 50rpx;
				margin-right: 180rpx;
			}
			.down{
				margin-top: 95rpx;
				width: 700rpx;
				height: 230rpx;
				background-color: #FFFFFF;
				border-radius: 30rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				box-shadow: 0px 2rpx 8rpx 0px rgba(165, 165, 165, 0.5);
				.info{
					display: flex;
					width: 550rpx;
					justify-content: space-between;
					.item{
						display: flex;
						flex-direction: column;
						justify-content: space-between;
						align-items: center;
						.itemInfo{
							display: flex;
							justify-content: space-between;
							align-items: center;
							font-size: 38rpx;
							font-weight: bold;
							width: 80rpx;
							height: 100rpx;
						}
					}
				}
				
			}
		}
	}
	.tag{
		width: 100%;
		height: 80rpx;
		padding-bottom: 35rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		.tagItem{
			width: 700rpx;
			height: 50rpx;
			display: flex;
			align-items: center;
			.icon{
				width: 200rpx;
				height: 80rpx;
				background-image: url('@/static/contact/friends.jpg');
				background-size: contain;
				background-repeat: no-repeat;
			}
		}
		
	}
</style>
