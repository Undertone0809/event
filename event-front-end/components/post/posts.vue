<template>
	<view class="postAll">
		<scroll-view scroll-y class="postAll" :refresher-enabled="false" enable-back-to-top lower-threshold=250 @scrolltolower="reachBottom()" :refresher-triggered="refresh" @refresherrefresh="pullDownRefresh()" >
			<view>
				<view v-if="!show">
					<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
						<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
							title loading avatar></u-skeleton>
					</view>
				</view>
				<view v-else>
					<view v-if="posts.length!=0" class="post">
						<view v-for="(item,index) in posts" :key="index">
						  <postItem :postInfo='item'/>
						</view>
						<view class="bottomBar">
							---没有了---
						</view>
					</view>
          <view v-else>
          <u-empty
            mode="search"
            icon="http://cdn.uviewui.com/uview/empty/search.png"
          >
          </u-empty>
          </view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
import postItem from '@/components/post/post-item'
import { getPostByUptime,hasLikePost,getPostBySearch,getFollowPost,getMyPost,getPostByViews } from '@/common/API.js'
export default {
	components: { postItem},
  props: {
    keyWord:{
    	type: String,
    	default(){ return '' }
    },
	fromPage:{
		type: String,
		default(){ return '' }
	}
  },
  data: () => ({
		show:false,
		posts:[],
		page:1,
		noMore:false, 
		refresh:false
  }),
  mounted(){
    this.$disp.on(`event.fresh_myPosts`,()=>{
      this.initData()
    })
    this.$disp.on(`event.fresh_contact`,()=>{
      this.initData()
    })
	 	this.initData()
  },
  methods:{
    initData(){
      if(!uni.getStorageSync(`userInfo`)){
        return
      }else if(this.fromPage=='contact'){
        getFollowPost(uni.getStorageSync('userId'),this.page,5).then(res=>{
          this.posts=res
          this.show = true
          this.refresh = false;
        },err=>{
          console.error(err)
        })
      }else if(this.fromPage=='myPosts'||this.fromPage=='hisPosts'){
        getMyPost(this.keyWord,this.page,5).then(res=>{
          this.posts=res
          this.show = true
          this.refresh = false;
        },err=>{
          console.error(err)
        })
      }else if(this.fromPage=='home'){
        getPostByViews(this.page,5).then(res=>{
            this.posts=res
            this.show = true
            this.refresh = false;
        },err=>{
          console.error(err)
        })
      }else{
        const p={
          "keyword": this.keyWord,
          "pageNum": this.page,
          "pageSize": 5
        }
        getPostBySearch(p).then(res =>{
          this.posts=res
          this.show = true
          this.refresh = false;
          console.log(this.posts)
        },err=>{
          console.error(err)
        })
      }
    


    },
	  getMoreData(){
	    this.page++;
		if(this.fromPage=='contact'){
			getFollowPost(uni.getStorageSync('userId'),this.page,5).then(res=>{
				if (res.length) this.posts = this.posts.concat(res)
				else this.noMore = true
			},err=>{
				console.log(err)
			})
		}else if(this.fromPage=='myPosts'){
			getMyPost(uni.getStorageSync('userId'),this.page,5).then(res=>{
				if (res.length) this.posts = this.posts.concat(res)
				else this.noMore = true
			},err=>{
				console.log(err)
			})
		}else if(this.fromPage=='home'){
			getPostByViews(this.page,5).then(res=>{
				if (res.length) this.posts = this.posts.concat(res)
				else this.noMore = true
			},err=>{
				console.log(err)
			})
		}else{
			const p={
						"keyword": this.keyWord,
						"pageNum": this.page,
						"pageSize": 5
					}
			getPostBySearch(p).then(res=>{
					if (res.length) this.posts = this.posts.concat(res)
					else this.noMore = true
			},err=>{
					console.log(err)
			})
		}
		
	  },
		reachBottom() {
			console.log(this.noMore)
			if(this.noMore) return;
			this.getMoreData();
		},
		pullDownRefresh() {
			this.page = 1
			this.noMore = false;
			this.refresh = true;
			if(this.fromPage=='contact'){
				getFollowPost(uni.getStorageSync('userId'),this.page,5).then(res=>{
					this.posts = res;
					this.refresh = false;
				})
			}else if(this.fromPage=='myPosts'){
				getMyPost(uni.getStorageSync('userId'),this.page,5).then(res=>{
					this.posts = res;
					this.refresh = false;
				})
			}else if(this.fromPage=='home'){
				getPostByViews(this.page,5).then(res=>{
					this.posts = res;
					this.refresh = false;
				})
			}else{
				const p={
					"keyword": this.keyWord,
					"pageNum": this.page,
					"pageSize": 5
				}
				getPostBySearch(p).then(res => {
					this.posts = res;
					this.refresh = false;
				})
			}
			
		},
  }
}
</script>

<style lang='scss'>
	.postAll{
		height: 100%;
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
	}
</style>