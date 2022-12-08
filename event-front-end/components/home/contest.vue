<template>
	<view class="all">
		<scroll-view scroll-y class="all" refresher-enabled enable-back-to-top lower-threshold=250 @scrolltolower="reachBottom()" :refresher-triggered="refresh" @refresherrefresh="pullDownRefresh()">
			<view class="flex-col">
				
				<view v-if="show">
					<view style="margin-top:200rpx;" v-if="!contestList.length">
						<u-empty
						  mode="news"
						  icon="http://cdn.uviewui.com/uview/empty/news.png"
						  text="暂无比赛"
						/>
					</view>
					<view v-else class="content" v-for="(item,index) in contestList" :key="item.contestId" @click="toDetail(item)">
						<view class="contestItem">
							<view class="left">
								<view class="order">
									<view class="icon1" v-if="index<3">
										{{index+1}}
									</view>
									<view class="icon2" v-else>
										{{index+1}}
									</view>
								</view>
								<view class="content">
									<view class="title">
										{{item.contestTitle}}
									</view>
									<view class="text">
										{{item.contestDescription}}
									</view>
									<view class="info">
										<view class="fire">
											<image style="width: 30rpx;height: 30rpx;" src="../../static/post/fire-fill.png" mode="aspectFill"></image>
											{{item.contestView}}
										</view>
										 <view v-if="compareTime(item.contestEndTime)==true" class="status" style="background-color: #f4524a;">
										 	已过期
										 </view>
										 <view v-else class="status" style="background-color:#2cc900;">
										 	进行中
										 </view>
									</view>
								</view>
							</view>
							
							<image v-if="item.contestImageUrl" :src="item.contestImageUrl" mode="aspectFill" class="right"></image>
						</view>
					</view>
				</view>

				<view v-else>
					<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
						<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
							title loading avatar></u-skeleton>
					</view>
				</view>
				
			</view>
		
      <blank-placeholder/>
    </scroll-view>
	</view>
</template>

<script>
  import blankPlaceholder from 'components/common/blank-placeholder'
	import {getTeam,getAllContest,getContestOrder} from '@/common/API.js'
	import { FRESH_CONTEST_COMPONENT } from 'common/topic_enum.js'
	export default {
    components:{
      blankPlaceholder
    },
		data() {
			return {
				refresh: false,
				contestList: [], 
				show: true,
				page: 0,
				noMore: false,
				now:new Date(),
				page:1,
				noMore:false,
			}
		},
		mounted() {
			getContestOrder(this.page,5).then(rs => {
				this.contestList = rs;
				this.show = true;
			},err=>{
				console.log(err)
			})
		},
		methods: {
			getMoreData(){
				this.page++;
				getContestOrder(this.page,5).then(res=>{
							if (res.length) this.contestList = this.contestList.concat(res)
							else this.noMore = true
						   },err=>{
							console.log(err)
						   })
			},
			reachBottom() { //分页处理
				if(this.noMore) return;
				this.getMoreData();
			},
			async pullDownRefresh() {
				this.page = 1
				this.noMore = false;
				this.refresh = true;
				getContestOrder(this.page,5).then(res => {
					this.contestList = res;
					this.refresh = false;
				})
			},
			compareTime(e){
				const date = new Date(e);
				if(date.getTime() < Date.now()){
				    return true
				}else{
				    return false
				}
			},
			toDetail(item){
				console.log(666)
				let contestItem=encodeURIComponent(JSON.stringify(item))
				uni.navigateTo({url:`/subPackageA/pages/home/contestDetail?contestItem=`+contestItem})
			}
		},
	}
</script>
<style lang="scss" scoped>
	.all {
		height: 100%;
		flex: 1;
	}
	.content {
		width: 100%;
		height: 100%;
		display: flex;
		justify-content: center;
		.contestItem{
			width: 93%;
			box-shadow: 0px 2rpx 8rpx 0px rgba(165, 165, 165, 0.5);
			border-radius: 18rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-top: 30rpx;
      overflow: hidden;

			.left{
				width: 500rpx;
				display: flex;
				.order{
					margin-top: 30rpx;
					margin-left: 20rpx;
					.icon1{
						width: 35rpx;
						height: 35rpx;
						display: flex;
						justify-content: center;
						align-items: center;
						background-color: #f4524a;
						color: #FFFFFF;
						font-size: 30rpx;
						border-radius: 5rpx;
					}
					.icon2{
						width: 35rpx;
						height: 35rpx;
						display: flex;
						justify-content: center;
						align-items: center;
						background-color: #FFFFFF ;
						color: #f4524a;
						font-size: 30rpx;
						border-radius: 5rpx;
					}
				}
				.content{
					width: 500rpx;
					display: flex;
					flex-direction: column;
					.title{
						width: 450rpx;
						font-size: 36rpx;
						padding: 20rpx;
					}
					.text{
						width: 450rpx;
						font-size: 28rpx;
						color: #888;
						padding: 0 20rpx;

            word-break: break-all;
            text-overflow: ellipsis;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 3; /* 这里是超出几行省略 */
					}
					.info{
						width: 380rpx;
						height: 66rpx;
						padding-left: 20rpx;
						font-size: 26rpx;
						color: #888;
						display: flex;
						justify-content: space-between;
						align-items: center;
						.fire{
							display: flex;
							align-items: center;
						}
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
				}
			}
			.right{
				width: 180rpx;
				height: 150rpx;
				margin-right: 10rpx;
        border-radius: 10rpx;
			}
		}
	}
</style>
