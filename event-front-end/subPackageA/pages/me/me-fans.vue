<template>
	<view>
		<view class="list-item" v-for="item in fans" :key="item.userid">
			<view @click="goUser(item)" class="left">
				<image class="avatar" :src="item.avatar" />
				<view class="nickname"><text>{{item.openname}}</text></view>
			</view>
<!-- 			<view @click="unFollow(item)" class="right" :style="{backgroundColor:bgc,color:fontColor}">
				<text>{{btnText}}</text>
			</view> -->
		</view>
	</view>
</template>

<script>
	import {getFans,followHim,unfollowHim} from "@/common/API.js"
	export default {
		data() {
			return {
				fans:[],
				btnText:'已关注',
				bgc:'#fff',
				fontColor: '#b3b3b3'
			};
		},
		onLoad(option) {
			getFans(option.userId).then(rs => {
				this.fans = rs
			})
		},
		methods: {
			unFollow() {
				this.btnText = '关注'
				this.bgc = '#FEE535'
				this.fontColor = '#5C5C5C'
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
		}
	}
</style>
