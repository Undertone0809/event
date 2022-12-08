<template>
	<view class="all">
		<users ref="more" fromPage="myFollows"></users>
	</view>
</template>

<script>
	import users from "@/components/contact/users"
	import {getContestOrder} from '@/common/API.js'
	export default{
		components: { users},
		data(){
			return{
				lists:[]
			}
		},
		onLoad(){
			
		},
		onReachBottom() {
			this.$refs.more.reachBottom();
		},
		methods:{
			
		}
	} 
</script>

<style lang="scss" scoped>
	.all{
		padding-top: 30rpx;
	}
</style>