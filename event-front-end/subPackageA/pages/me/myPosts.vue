<template>
	<view class="all">
		<posts ref="more" :keyWord="userId" fromPage="myPosts"></posts>
	</view>
</template>

<script>
	import posts from "@/components/post/posts.vue"
	import {getContestOrder} from '@/common/API.js'
	export default{
		components: { posts},
		data(){
			return{
				lists:[],
				userId:''
			}
		},
		onLoad(option){
			this.userId=option.userId;
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