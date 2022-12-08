<template>
	<view class="flex-col page">
		<view class="flex-col">
			<text class="title">通用</text>
			<view class="bottom-section flex-col view">
				<view @click="goSecurity()" class="top-group justify-between view_1">
					<text>帐号与安全</text>
					<image
					src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16424068065424798350.png"
					class="image image_1" />
				</view>
				<!-- #ifdef MP-WEIXIN -->
				<view class="divider"></view>
				<view @click="goYimu()" class="top-group justify-between view_1">
					<text>一目校园</text>
					<image
					src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16424068065424798350.png"
					class="image image_1" />
				</view>
				<!-- #endif -->
			</view>
		</view>
		<view class="flex-col whe">
			<text class="title">关于我们</text>
			<view class="bottom-section flex-col view">
				<view @click="goContact()" class="top-group justify-between view_1">
					<text>联系反馈</text>
					<image
						src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16424068065424798350.png"
						class="image image_1" />
				</view>
				<view class="divider"></view>
				<view @click="goPrivacy()" class="top-group justify-between view_1">
					<text>隐私政策</text>
					<image
					src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16424068065424798350.png"
					class="image image_1" />
				</view>
				<view class="divider"></view>
				<view @click="goGroup()" class="top-group justify-between view_1">
					<text>开发团队</text>
					<image
					src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16424068065424798350.png"
					class="image image_1" />
				</view>

			</view>
		</view>
			<view class="flex-col whe">
				<text class="title">其他</text>
				<view class="bottom-section flex-col view">
					<view @click="clearStorage()" class="top-group justify-between view_1">
						<text>清理缓存</text>
					</view>
					<view class="divider"></view>
					<view @click="logout()" class="top-group justify-between view_1">
						<text>退出登录</text>
					</view>
				</view>
			</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {};
		},
		methods: {
			goSecurity() {
				uni.navigateTo({url:'/subPackageA/pages/me/setting/me-security'})
			},
			goYimu() {
				uni.navigateToMiniProgram({appId: 'wx0039159c9e7dacc1'})
			},
			goContact() {
				uni.navigateTo({url:'/subPackageA/pages/me/setting/me-contact'})
			},
			goPrivacy() {
				uni.navigateTo({url:'/subPackageA/pages/me/setting/me-privacy'})
			},
			goGroup() {
				uni.navigateTo({url:'/subPackageA/pages/me/setting/me-devGroup'})
			},
			clearStorage() {
				uni.showModal({
					title: '提示',
					content: '清理缓存将会清空消息列表和未读消息,消息记录会依然保留',
					success: function (res) { if (res.confirm) 
						uni.setStorageSync('msgList',[]); 
						uni.setStorageSync('tempMsg',[])
						uni.setStorageSync('unreadNum',0) }
				});
			},
			logout() {
				const that = this
				uni.showModal({
					title: '',
					content: '确定要退出登录？',
					success: function (res) { if (res.confirm) {
						uni.closeSocket();
						uni.clearStorage();
						that.$store.commit("setLoginStatus", false)
						that.$store.commit("setUserId", '')
						uni.navigateBack({})
					} }
				});
			}
		}
	};
</script>

<style scoped lang="scss">

	.page {
		padding: 35rpx 0 92rpx;
		background-color: #00000008;
		width: 100%;
		height: 100vh;
		overflow-y: auto;
		font-weight: 400;

		.title{
			margin-left: 44rpx;
			color: rgb(157, 154, 153);
			font-size: 26rpx;
			line-height: 36rpx;
			white-space: nowrap;
			font-weight: 400;
		}

		.view {
			margin-top: 4rpx;
		}
		
		.whe {
			margin-top: 45rpx;
		}

		.bottom-section {
			padding-left: 42rpx;
			background-color: #fff;

			.top-group {
				padding: 42rpx 0 29rpx;

				.image_1 {
					margin-right: 42rpx;
					margin-top: 8rpx;
				}
			}

			.view_1 {
				color: rgb(23, 36, 52);
				font-size: 28rpx;
				line-height: 44rpx;
				white-space: nowrap;
			}

			.image {
				width: 12rpx;
				height: 18rpx;
			}
		}

		.divider {
			align-self: flex-end;
			background-color: #dbdede;
			width: 568rpx;
			height: 2rpx;
		}
	}
</style>
