<template>
	<view class="nologin" :style="{paddingTop:paddingTop+'rpx'}">
		<image style="width: 100%; height: 803rpx;" src="https://s2.loli.net/2022/01/25/xSY4NX39RjEmfbr.png"></image>
		<view class="flex-col items-center">
			<text class="title">未登录</text>
			<text class="text">您需要先登录才可以使用完整功能！</text>
		</view>
		<view @click="goLogin()" class="flex-col items-center btn">
			<text>去登录</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				paddingTop:177
			}
		},
		mounted() {
			let cur = getCurrentPages().pop()
			console.log(cur.route);
			if(!cur.route.includes('me/me')) this.paddingTop = 0
		},
		methods: {
			goLogin() {
				uni.navigateTo({
					url: '/subPackageA/pages/me/login'
				})
			}
		}

	}
</script>

<style lang="scss">
	.nologin {
		display: flex;
		flex-direction: column;
		align-items: center;
		background-color: #ffffff;
		padding-top: 100rpx;

		.btn {
			margin-top: 70rpx;
			padding: 26rpx 0 20rpx;
			align-self: center;
			color: rgb(255, 255, 255);
			font-size: 30rpx;
			font-weight: 600;
			line-height: 42rpx;
			white-space: nowrap;
			background-color: #ff5757;
			box-shadow: 0px 26rpx 50rpx 0px rgba(86, 102, 194, 0.16);
			border-radius: 44rpx;
			width: 244rpx;
		}

		.title {
			color: rgb(48, 44, 72);
			font-size: 66rpx;
			font-weight: 600;
			line-height: 92rpx;
			white-space: nowrap;
		}

		.text {
			margin-top: 30rpx;
			color: rgba(48, 44, 72, 0.7);
			font-size: 30rpx;
			font-weight: 600;
			line-height: 50rpx;
			text-align: center;
			width: 330rpx;
		}
	}
</style>
