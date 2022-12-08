<template>
	<view class="">
		<top-nav class="nav"></top-nav>
		<view class="bar">
		
		</view>
		<view class="title">
			<view class="titleText">
				猜你想看
			</view>
		</view>

    <!-- 四个比赛 -->
		<view class="list">
			<view class="listContainer">
				<view class="listItem" v-for="(item,index) in lists" @click="toContest(item)">
					{{item.contestTitle}}
				</view>
			</view>
		</view>

    <!-- 想法 -->
    <!-- <view v-for="(item,index) in postList" :key="index">
      <post-item :postInfo='item'/>
    </view> -->
    <all ref="more" fromPage="searchHub"></all>
	</view>
</template>

<script>
	import team from "@/components/home/team.vue"
	import topNav from "@/components/home/topNav.vue"
	import segmenter from "@/components/common/segmenter.vue"
	import all from "@/components/home/all.vue"
  import postItem from '@/components/post/post-item.vue'
	import {getContestOrder,getPostByViews} from '@/common/API.js'
	export default{
		components: { topNav,team,segmenter,postItem,all },
		data(){
			return{
				lists:[],
        postIndex:0,
        postList:[],
        postNoMore:false,
			}
		},
		onLoad(){
      this.initData()
		},
		onReachBottom() {
			this.$refs.more.reachBottom();
		},
    /**
     * 开发中，勿动！！！！！！！！！！！！！！！！！！！！！！！
     */
		methods:{
      initData(){
        getContestOrder(1,4).then(rs => {
          this.lists = rs;
        },err=>{
          console.log(err)
        })

        this.getMorePost()
      },
      getMorePost(){
        getPostByViews(this.postIndex+1,5).then(rs=>{
          this.postList = this.postList.concat(rs)
          this.postIndex = this.postIndex+1
          if(rs.length<5){
            this.postNoMore = true
          }
        })
      },
			toContest(item){
				let contestItem=encodeURIComponent(JSON.stringify(item))
				uni.navigateTo({url:`/subPackageA/pages/home/contestDetail?contestItem=`+contestItem})
			}
		}
	} 
</script>

<style lang="scss" scoped>
	.seg {
		white-space: initial;
	}
	.bar{
		width: 100%;
		height: 20rpx;
		background-color: #f6f6f6;
	}
	.title{
		width: 100%;
		height: 80rpx;
		display: flex;
		align-items: center;
		.titleText{
			width: 200rpx;
			height: 100rpx;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 38rpx;
			font-weight: bold;
			
		}
	}
	.list{
		width: 100%;
		display: flex;
		justify-content: center;
		.listContainer{
			width: 700rpx;
			display: flex;
			justify-content: space-between;
			flex-wrap: wrap;
			.listItem{
				border-bottom: solid 2px #f6f6f6;
				width: 340rpx;
				padding: 20rpx 0;
				white-space:nowrap;
			    overflow:hidden;
			    text-overflow:ellipsis;
				
			}
		}
	}
</style>