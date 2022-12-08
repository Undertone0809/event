<template>
		<view>
			<view id="top">
				<!-- 顶部导航栏 -->
				<top-nav class="nav" :inHome="true"></top-nav>
				
				<view class="seg">
					<segmenter ref="seg" :tabData="tabs" :activeIndex="activeIndex" :config="config"
						@tabClick="tabClick($event, '默认设置')" />
				</view>
			</view>
			<view>
				<view class="contentMain" >
					<view class="flex-col content" :style="{height:swiperHeight+'rpx'}">
						<swiper class="swiper-box" :current="activeIndex" @change="updateIndex" >
							<!-- 全部 -->
							<swiper-item class="swiper-item">
								<!-- <posts fromPage="home"></posts> -->
								<all fromPage="home"></all>
							</swiper-item>
							<!-- 组队 -->
							<swiper-item class="swiper-item">
								<team id="box"></team>
							</swiper-item>
							<!-- 附近-->
							<swiper-item class="swiper-item">
								<neighbor></neighbor>
							</swiper-item>
							<!-- 比赛 -->
							<swiper-item class="swiper-item">
								<contest></contest>
							</swiper-item>
						</swiper>
					</view>
				</view>
			</view>
		<tabbar id="bottom"></tabbar>
	</view>
		
</template>

<script>
	
	import {checkToken,getNotFinishedTeam} from '@/common/API.js'
	import topNav from "@/components/home/topNav.vue"
	import segmenter from "@/components/common/segmenter.vue"
	import team from "@/components/home/team.vue"
	import contest from "@/components/home/contest.vue"
	import neighbor from "@/components/home/neighbor.vue"
	import tabbar from "@/components/home/tabbar.vue"
	import posts from "@/components/post/posts.vue"
	import all from "@/components/home/all.vue"
  import { FRESH_TEAM_COMPONENT,FRESH_NEIGHBOR_COMPONENT,FRESH_CONTEST_COMPONENT } from 'common/topic_enum.js'
	export default {
		components: { topNav, segmenter, team, contest, neighbor ,tabbar,posts,all},
		data() {
			return {
				swiperHeight: 0,
				topHeight:0,
				bottomHeight:0,
				// 分段器配置
				activeIndex: 0,
				tabsArray: ['组队广场', '附近人才', '比赛专区','闲聊吐槽'],
				tabs: [
					{state: -1,name: '发现'},
					{state: 1,name: '组队'},
					{state: 2,name: '附近'}, 
					{state: 3,name: '比赛'},
				],
				config: {
					key: 'name', // 要显示的key
					fontSize: 30, // 字体大小 rpx
					color: '#7d7d7f', // 字体颜色
					activeColor: '#242424', // 激活字体颜色
					itemWidth: 0, // item宽度 0为自动
					underLinePadding: 10, // 下划线左右边距，文字宽度加边距，如果设置了itemWidth则为itemWidth加边距 rpx
					underLineWidth: 80, // 下划线宽度 rpx  注意：设置了此值 underLinePadding 失效
					underLineHeight: 10, // 下划线高度 rpx
					underLineColor: 'linear-gradient(270deg, #feaeb1 0%, #f6585b 100%)' // 下划线颜色
				},
				tabbar:''
			}
		},
    // 分享给好友
    onShareAppMessage() {
      return {
        title: '组队广场',
        path: '/pages/home/home',
      }
    },
		async onPullDownRefresh() {
			this.page = 0
			this.show = false
			await getNotFinishedTeam(5,this.page).then(rs => { this.teamList = rs; uni.stopPullDownRefresh() })
			this.$nextTick(() => {this.show=true})
		},
		onLoad() {
			if(uni.getStorageSync("hasLogin")){
				this.$store.commit("setLoginStatus",true)
			}
			var windowHeight = uni.getSystemInfoSync().windowHeight;
			var windowWidth = uni.getSystemInfoSync().windowWidth;
			this.init();
			var windowHeight = uni.getSystemInfoSync().windowHeight;
			var windowWidth = uni.getSystemInfoSync().windowWidth;
			var windowHeight = uni.getSystemInfoSync().windowHeight;
			var windowWidth = uni.getSystemInfoSync().windowWidth;
			const query = uni.createSelectorQuery().in(this);
			query.select('#top').boundingClientRect(data => {
				this.swiperHeight = parseInt(((windowHeight - 51 - data.height + 1) * (750 / windowWidth))); 
			}).exec();
		},
    onShow(){
      // if(this.activeIndex==0) this.$disp.fire(FRESH_TEAM_COMPONENT)
      // if(this.activeIndex==1) this.$disp.fire(FRESH_NEIGHBOR_COMPONENT)
      // if(this.activeIndex==2) this.$disp.fire(FRESH_CONTEST_COMPONENT)
    },
		methods: {
			init() {
				if (!this.$store.state.hasLogin||!uni.getStorageSync('token')){
          uni.navigateTo({url: '/subPackageA/pages/me/login'})
          return
        }else {
			checkToken(uni.getStorageSync('token')).then(bool=>{
            if(!bool) {
              uni.$u.toast('身份信息已过期')
              console.log(`[info] Identity has expired,remove storage userInfo`)
              uni.navigateTo({url:'/subPackageA/pages/me/login'})
              uni.removeStorageSync('userInfo')
              uni.setStorageSync({key:'hasLogin',data:false})
              uni.removeStorageSync('token')
            }else{
              console.log(`[info] identity has login`)
            }
          }) 
				}
			},
			tabClick(index, name) {
				if(index==2){
					if(!uni.getStorageSync("hasLogin")){
					  uni.navigateTo({url: '/subPackageA/pages/me/login'});
					}
					else{
						this.activeIndex = index
					}
				}else this.activeIndex = index
			},
			updateIndex(e) {
				this.activeIndex = e.detail.current
				this.$refs.seg.tabClick(this.activeIndex)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.contentMain{
		display: flex;
		flex-direction: column;
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
	}
	.refreshTip {
		margin-top: 60rpx;
		height: 110rpx;
		display: flex;
		align-items: flex-end;
		justify-content: center;
		color: #bcbabd;
		
		text {
			line-height: 34rpx;
			margin-left: 18rpx;
		}
	}
</style>
