<template>
	<view class="all">
		<view v-if="show">
			<view class="top" id="all">
				<view class="title">
					{{item.contestTitle}}
					<view style="display: inline-block;margin-left: 15rpx;">
						<view v-if="compareTime(item.contestEndTime)==true" class="status" style="background-color: #f4524a;">
							已过期
						</view>
						<view v-else class="status" style="background-color:#2cc900;">
							进行中
						</view>
					</view>
				</view>
				<view class="content">
					<view class="img">
						<image class="img" :src="item.contestImageUrl" mode="widthFix"></image>
					</view>
					<view class="description">
						{{item.contestDescription}}
					</view>
				</view>
			</view>
			<view class="mid">
				<view class="info">
			<view class="fire left">
			  <image style="width: 30rpx;height: 30rpx;" src="../../../static/post/fire-fill.png" mode="aspectFill"></image>
			  {{item.contestView}}
			  <view class='left2'>浏览</view>
			</view>
					<view class="fire">
						开始时间：{{formateTime(item.contestBeginTime)}}
					</view>
					<view class="fire">
						结束时间：{{formateTime(item.contestEndTime)}}
					</view>
				</view>
			</view>
		</view>
		<view v-else>
			<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
				<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
					title loading avatar></u-skeleton>
			</view>
		</view>
		
		<!-- <view class="seg">
			<segmenter ref="seg" :tabData="tabs" :activeIndex="activeIndex" :config="config"
				@tabClick="tabClick($event, '默认设置')" />
		</view>
		<view>
			<view class="contentMain" >
				<view class="flex-col content" :style="{height:swiperHeight+'vh'}">
					<swiper class="swiper-box" :current="activeIndex" @change="updateIndex" > -->
						<!-- 全部 -->
						<!-- <swiper-item class="swiper-item">
							<posts fromPage="contestDetail"></posts>
						</swiper-item> -->
						<!-- 组队 -->
						<!-- <swiper-item class="swiper-item">
							<team :keyWord="item.contestId" fromPage="contestDetail" id="box"></team>
						</swiper-item>
					</swiper>
				</view>
			</view>
		</view> -->
		<all fromPage="contestDetail" :keyWord="item.contestId"></all>
		<view class="come">
			<u-tag text="发起组队" icon="edit-pen-fill" size="large" shape="circle" borderColor="#c35556" bgColor="#c35556" @click="toCreate()"></u-tag>
		</view>
	</view>
</template>

<script>
	import team from "@/components/home/team.vue"
	import posts from "@/components/post/posts.vue"
	import segmenter from "@/components/common/segmenter.vue"
	import all from "@/components/home/all.vue"
	import {} from '@/common/API.js'
	export default {
		components:{team,segmenter,posts,all},
		data() {
			return {
				show:false,
				item:{},
				swiperHeight: 100,
				topHeight:0,
				bottomHeight:0,
				// 分段器配置
				activeIndex: 0,
				activeIndex: 0,
				tabs: [
					{state: -1,name: '全部'},
					{state: 1,name: '组队'},
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
				refresh:false
			};
		},
		onPullDownRefresh() {
			
		},
		 onLoad(option) {
			 this.item=JSON.parse(decodeURIComponent(option.contestItem))
			 this.show=true
			 var windowHeight = uni.getSystemInfoSync().windowHeight;
			 var windowWidth = uni.getSystemInfoSync().windowWidth;
			 const query = uni.createSelectorQuery().in(this);
			 // query.select('#top').boundingClientRect(data => {
			 // 	this.swiperHeight = parseInt(((windowHeight - 51 - data.height + 1) * (750 / windowWidth))); 
			 // }).exec();
		},
		methods: {
			compareTime(e){
				const date = new Date(e);
				if(date.getTime() < Date.now()){
				    return true
				}else{
				    return false
				}
			},
			formateTime(e){
				
				return this.$zee.timestampFormat(e , 'yyyy/mm/dd')
			},
			toCreate(){
				uni.navigateTo({
					url:'../team/teamSend'
				})
			},
			
			tabClick(index, name) {
				// if(index == 3 ) {
				// 	uni.navigateTo({url:'../post/post'})
				// 	this.$refs.seg.tabClick(this.activeIndex)
				// }
				// else 
				this.activeIndex = index
			},
			updateIndex(e) {
					this.activeIndex = e.detail.current
					this.$refs.seg.tabClick(this.activeIndex)
			// 提示对应组件刷新
			// if(this.activeIndex==0) this.$disp.fire(FRESH_TEAM_COMPONENT)
			// if(this.activeIndex==1) this.$disp.fire(FRESH_NEIGHBOR_COMPONENT)
			// if(this.activeIndex==2) this.$disp.fire(FRESH_CONTEST_COMPONENT)
				},
		}
	};
</script>

<style lang="scss" scoped>
	.all{
		.top{
			width: 100%;
			display: flex;
			flex-direction: column;
			align-items: center;
			.title{
				width: 700rpx;
				font-size: 40rpx;
				font-weight: bold;
				padding: 30rpx 0;
				word-break: break-word;
				display: flex;
				align-items: center;
				.status{
					width: 100rpx;
					height: 35rpx;
					background-color: #2a928a;
					display: flex;
					justify-content: center;
					align-items: center;
					color: #FFFFFF;
					border-radius: 6rpx;
					font-size: 26rpx;
				}
			}
			.content{
				width: 700rpx;
				display: flex;
				flex-direction: column;
				align-items: center;
				padding-bottom: 30rpx;
				border-bottom: solid 2rpx rgba(0, 0, 0, 0.1);
				.description{
					width: 700rpx;
					word-break: break-word;
					font-size: 32rpx;
					color: #616161;
				}
				.img{
					width: 700rpx;
					border-radius: 15rpx;
				}
			}
		}
		.mid{
			width: 100%rpx;
			display: flex;
			justify-content: center;
			border-bottom: solid 20rpx #f6f6f6;
			color: #616161;
			.info{
				width: 700rpx;
				display: flex;
				justify-content: space-between;
				font-size: 24rpx;
				padding: 30rpx 0;
				.fire{
					display: flex;
					align-items: center;
				}
        .left{
          color: #444;
          font-weight: 700;
        }
				.left2{
          color:#616161;
          font-weight:500;
        }
			}
			
		}
		.seg {
			white-space: initial;
		}
		.come{
			width: 100%;
			display: flex;
			justify-content: center;
			position: fixed;
			bottom: 100rpx;
			
		}
		.contentMain{
			
			display: flex;
			flex-direction: column;
			.content {
				padding-top: 0rpx;
				
				.swiper-box {
					flex: 1;
					
					.swiper-item {
						height: 100%;
					}
				}
					
			}
		}
		
	}
</style>