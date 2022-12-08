<template>
	<view>
		<view class="list-item" v-for="item in follows" :key="item.userid">
			<view @click="goUser(item)" class="left">
				<image class="avatar" :src="item.avatar" />
				<view class="nickname"><text>{{item.openname}}</text></view>
			</view>
			<!-- <view @click="follow(item)" :class="{right2:item.followState=='关注'}" class="right">
				<text>{{item.followState}}</text>
			</view> -->
		</view>
	</view>
</template>

<script>
	import {getFollow,followHim,unfollowHim,hasFollowed} from "@/common/API.js"
	export default {
		data() {
			return {
				userId:0,
				follows:[],
			};
		},
		onLoad(option) {
			this.userId = option.userId
			getFollow(option.userId).then( rs => {
				this.follows = rs
				// rs.forEach(async item => {
				// 	await hasFollowed(uni.getStorageSync('userId'),item.userid).then(bool => {
				// 		item.followState = bool ? '已关注' : '关注'
				// 	})
				// 	this.follows = rs
				// 	this.$forceUpdate()
				// })

			})
		},
		methods: {
			follow(item) {
				if(item.followState=='已关注') {
					uni.showActionSheet({
						itemList:['取消关注'],
						itemColor:'#fc2931',
						success: () => {
							item.followState = '关注'
							unfollowHim(uni.getStorageSync('userId'),item.userid)
						}
					})
				}
				else {
					item.followState = '已关注'
					followHim(uni.getStorageSync('userId'),item.userid)
				}
				
				
			},
			goUser(item) {
				uni.navigateTo({url:'/subPackageA/pages/me/user?userId='+item.userid})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.list-item {
		width: 100%;
		height: 120rpx;
		padding:20rpx;
		border-bottom: 1px #F9F9F9 solid;
		display: flex;
		align-items: center;
		justify-content: space-between;
		
		.left {
			display: inline-flex;
			
			.avatar {
				width: 80rpx;
				height: 80rpx;
				border-radius: 50%;
				border: 1px #F9F9F9 solid;
			}
			.nickname {
				font-size: 32rpx;
				color: #5C5C5C;
				line-height: 80rpx;
				font-weight: 500;
				margin-left: 27rpx;
			}
		}
		
		.right {
			width: 146rpx;
			height: 58rpx;
			border: 1px #F1F1F1 solid;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 27rpx;
			border-radius: 34rpx;
			box-shadow: 1px 1px 1px 1px #f9f9f9;
			background-color: #fff;
			color: #b3b3b3;
		}
		
		.right2 {
			width: 146rpx;
			height: 58rpx;
			border: 1px #F1F1F1 solid;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 27rpx;
			border-radius: 34rpx;
			box-shadow: 1px 1px 1px 1px #f9f9f9;
			background-color: #FEE535;
			color: #5C5C5C;
		}
	}
</style>
