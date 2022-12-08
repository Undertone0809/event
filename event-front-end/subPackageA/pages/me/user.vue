<template>
	<view>
		<user-page v-if="show" :userInfo="userInfo"></user-page>
	</view>
</template>

<script>
	import userPage from "@/components/me/user-page.vue"
	import {getUserById,getUserIntro} from "@/common/API.js"
	export default {
		components: {userPage},
		data() {
			return {
				userId: 0,
				userInfo: {},
				show: false
			};
		},
    // 分享给好友
    onShareAppMessage() {
      return {
        title: '快来看看ta的个人名片',
        path: '/subPackageA/pages/me/user?userId='+this.userId,
      }
    },
		async onLoad(option) {
      this.userId = option.userId
			var t = ''
			await getUserById(this.userId).then(rs => {t = rs})
			await getUserIntro(this.userId).then(rs => {this.userInfo = {...t,...rs}})
			this.show = true
		}
	}
</script>

<style lang="scss">

</style>
