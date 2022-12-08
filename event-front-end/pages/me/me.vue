<template>
	<view>
		<view>
			<!-- 未登录 -->
			<template v-if="showLogin">
				<nologin></nologin>
			</template>
		
			<!-- 登录后 -->
			<template v-else>
				<user-page ref="son" :userInfo="userInfo"></user-page>
			</template>
		</view>
		<tabbar></tabbar>
	</view>	
</template>

<script>
	import nologin from "@/components/me/nologin.vue"
	import userPage from "@/components/me/user-page.vue"
	import tabbar from "@/components/home/tabbar.vue"
	export default {
		components: {
			userPage,nologin,tabbar
		},
		data() {
			return {
				userInfo:{},
				showLogin: false
			};
		},
    // 分享给好友
    onShareAppMessage() {
      return {
        title: '我的个人名片',
        path: '/pages/home/home',
      }
    },
		onPullDownRefresh() {
			this.refresh()
		},
		onShow() {
			if(!uni.getStorageSync('hasLogin')) {this.showLogin = true;return;}
			this.showLogin = false
			this.userInfo = uni.getStorageSync('userInfo')
		},
		methods: {
			refresh() {
				if(!uni.getStorageSync('hasLogin')) return;
				this.$disp.fire(`event.fresh_page_me`)
				setTimeout(()=>{uni.stopPullDownRefresh()},700)
			},
		}
	};
</script>

<style scoped lang="scss">
	
</style>
