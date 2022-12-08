<template>
	<view class="">
		
		<view class="">
			<top-nav class="nav" :keyWord="key" fromPage="searchDetail"></top-nav>
			<view class="seg">
				<segmenter ref="seg" :tabData="tabs" :activeIndex="activeIndex" :config="config"
					@tabClick="tabClick($event, '默认设置')" />
			</view>
			<view class="flex-col content" :style="{height:`calc(100vh - ${top})`}">
				<swiper class="swiper-box" :current="activeIndex" @change="updateIndex" >
					<!-- 综合 -->
					<swiper-item class="swiper-item">
						<all :keyWord="key" fromPage="searchDetail"></all>
					</swiper-item>
					<!-- 组队 -->
					<swiper-item class="swiper-item">
						<team :keyWord="key" fromPage="searchDetail" id="box"></team>
					</swiper-item>
					<!-- 想法-->
					<swiper-item class="swiper-item">
						<posts :keyWord="key"></posts>
					</swiper-item>
					<!-- 用户-->
					<swiper-item class="swiper-item">
						<users :keyWord="key" fromPage="searchDetail"></users>
					</swiper-item>
				</swiper>
			</view>
		</view>
	</view>
	
</template>

<script> 
	
	import team from "@/components/home/team.vue"
	import topNav from "@/components/home/topNav.vue"
	import segmenter from "@/components/common/segmenter.vue"
	import postItem from 'components/post/post-item'
	import posts from '@/subPackageA/components/post/posts'
	import users from '@/components/contact/users.vue'
	import all from '@/components/home/all.vue'
	import { getPostByUptime,hasLikePost } from '@/common/API.js'
	export default{
		components: { topNav,team,segmenter,postItem,posts,users,all},
		data(){
			return{
				top: 0,
				// 分段器配置
				activeIndex: 0,
				tabsArray: ['综合','组队', '想法','用户'],
				tabs: [
					{state: -1,name: '综合'},
					{state: 0,name: '组队'},
					{state: 1,name: '想法'},
					{state: 2,name: '用户'}
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
				key:''
			}
		},
		onLoad(option){
			console.log(option)
			this.key=option.key;
			this.activeIndex=option.index;
			const query = uni.createSelectorQuery().in(this);
			query.select('#box').boundingClientRect(data => {
				this.top = '' + data.top + 'px'
			}).exec();
		},
		methods:{
			tabClick(index, name) { 
				this.activeIndex = index ;
			},
			updateIndex(e) {
				this.activeIndex = e.detail.current
				this.$refs.seg.tabClick(this.activeIndex)
			},
		}
	} 
</script>
 
<style  lang="scss" scoped>
.content {
		padding-top: 0rpx;
		
		.swiper-box {
			flex: 1;
		}
			
	}
	.seg {
		white-space: initial;
	}
	.refreshTip {
		margin-top: 60rpx;
		height: 110rpx;
		display: flex;
		align-items: flex-end;
		justify-content: center;
		color: #bcbabd;
		
		text {
			line-height: 34rpx;
			margin-left: 18rpx;
		}
	}
</style>