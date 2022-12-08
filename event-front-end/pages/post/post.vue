<template>
	<view>
		<view v-if="!show">
			<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
				<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
					title loading avatar></u-skeleton>
			</view>
		</view>
		<view v-else>
			<view class="post">
				<view class="top">
					<!-- <view class="poem">{{today.text}}</view> -->
					<image @click="preview()" style="width: 100%;" :src="today.src" mode="aspectFill"></image>
				</view>

        <view v-for="(item,index) in posts" :key="index">
          <post-item :postInfo='item'/>
        </view>

				<view class="bottomBar">
					---没有了---
				</view>
			</view> 
		</view>
		<tabbar/>
	</view>
		
	
</template>

<script>
import postItem from 'components/post/post-item'
import { FRESH_POST_PAGE } from 'common/topic_enum.js'
import { getPostByUptime,hasLikePost } from '@/common/API.js'
import tabbar from "@/components/home/tabbar.vue"
export default {
	components: { tabbar,postItem },
	data() {
		return {
			show:false,
			posts:[],
			idx:0,
			nomore:false, 
			today:{},
			likeIcon:"../../static/post/like.png"
		}
	},
  // 微信小程序分享
  onShareAppMessage() {
    return {
      title: '快来看看他们的想法',
      path: '/pages/post/post',
    }
  },
  // 分享到朋友圈
  onShareTimeline() {
    return {
      title: '快来看看他们的想法',
      path: '/pages/post/post',
    }
  },
  onShow() {
    // uni.$u.throttle(getCurrentPages().pop().onPullDownRefresh(), 500)

	},
	async onLoad() {
    // this.$disp.on(FRESH_POST_PAGE,()=>{
    //   var cur = getCurrentPages().pop()
    //   cur.onPullDownRefresh()
    // })
		uni.request({
			url:'https://apier.youngam.cn/essay/one',
			success:(res) => this.today = res.data.dataList[0]
		})
		await getPostByUptime({idx:this.idx}).then(res =>{
		this.posts=res
        this.show = true
  		this.idx+=5
		console.log(this.posts)
    })
	},
	onReachBottom() {
		if(this.nomore) return
		else getPostByUptime({idx:this.idx}).then(res => {
			if(!res) this.nomore = true
			else this.posts = this.posts.concat(res); 
		})
		this.idx+=5
	},
	onPullDownRefresh() {
		this.idx = 0
		this.nomore = false;
		getPostByUptime({idx:this.idx}).then(res => this.posts = res)
		setTimeout(() => {
			uni.stopPullDownRefresh()
		},400)
	},
	methods: {
		preview() {
			uni.previewImage({urls:[this.today.src]})
		}
		
	}
}
</script>

<style lang="scss" scoped>
	.top{
		width: 100%;
		height: 340rpx;
		
		.poem {
			position: absolute;
			text-align: center;
			font-size: 30rpx;
			color: #e4e4e4;
			text-shadow: 1px 1px #000000;
			font-weight: 600;
			font-style: italic;
			top: 0;
		}
	}
	.post{
		height: 100%;
		width: 100%;	
		padding-bottom: 30rpx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
	
	}
	.bottomBar {
		width: 100%;
		padding-bottom: 100rpx;
		padding-top: 80rpx;
		display: flex;
		justify-content: center;
		color: rgb(153, 153, 153);
	}
</style>
