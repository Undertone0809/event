<template>
	<view class="userAll">
		<scroll-view scroll-y class="userAll"  :refresher-enabled="fromPage!='searchDetail'?false:true" enable-back-to-top lower-threshold=250 @scrolltolower="reachBottom()" :refresher-triggered="refresh" @refresherrefresh="pullDownRefresh()" >
			<view>
				<view v-if="!show">
					<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
						<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
							title loading avatar></u-skeleton>
					</view>
				</view>
				<view v-else>
					<view v-if="userList.length!=0">
						<view class="itemContainer">
							<div class="friend-item" v-for="(item,index) in userList">
								<userItem :item="item"></userItem>
							</div>
							<view class="bottomBar">
								---没有了---
							</view>
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
	import {getFans,followHim,unfollowHim,getUserBySearch,getFollowedUser,getFansUser,recommendUser} from "@/common/API.js"
	import userItem from "@/components/contact/user-item.vue"
	export default {
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
	  components:{userItem},
	  data: () => ({
			show:false,
			userList:[],
			attentionSet:"＋关注",//关注按钮的内部文字
			isAttention:false,//判断是否关注的状态变量
			attentionColor:'#dd524d',//关注时的颜色
			isMe:false,
			hasFollowed:false,
			page:1,
			noMore:false,
			refresh:false
	  }),
	  mounted(){
      this.$disp.on(`event.fresh_contact`,() => {
        this.initData()
      })
      this.initData()
	  },
	  methods:{
      initData(){
        if(!uni.getStorageSync(`userInfo`)){
          return
        }else if(this.fromPage=='myFollows'){
          getFollowedUser(uni.getStorageSync('userId'),this.page,8).then(res=>{
            this.userList=res;
            this.show=true;
          },err=>{
            console.error(err)
          })
        }else if(this.fromPage=='myFans'){
          getFansUser(uni.getStorageSync('userId'),this.page,8).then(res=>{
            this.userList=res;
            this.show=true;
          },err=>{
            console.error(err)
          })
        }else if(this.fromPage=="searchDetail"){
        const p={"userId": uni.getStorageSync('userId'),"keyword": this.keyWord,"pageNum": 1,"pageSize": 8}
          getUserBySearch(p).then(res=>{
            this.userList=res;
            this.show=true;
          },err=>{console.error(err)})
        }else if(this.fromPage=='contact'){
          recommendUser(uni.getStorageSync('userId'),this.page,8).then(res=>{
            this.userList=res;
            this.show=true;
          },err=>{
            console.error(err)
          })
        }
      },
		  getMoreData(){
		    this.page++;
        if(this.fromPage=='myFollows'){
          getFollowedUser(uni.getStorageSync('userId'),this.page,8).then(res=>{
            if (res.length) this.userList = this.userList.concat(res)
              else this.noMore = true
            },err=>{
              console.error(err)
          })
        }else if(this.fromPage=='myFans'){
          getFansUser(uni.getStorageSync('userId'),this.page,8).then(res=>{
            if (res.length) this.userList = this.userList.concat(res)
              else this.noMore = true
            },err=>{
              console.error(err)
          })
        }else{
          recommendUser(uni.getStorageSync('userId'),this.page,8).then(res=>{
            if (res.length) this.userList = this.userList.concat(res)
            else this.noMore = true
            },err=>{
            console.error(err)
            })
        }
			
		  },
			reachBottom() {
				if(this.noMore) return;
				this.getMoreData();
			},
			pullDownRefresh() {
				this.page = 1
				this.noMore = false;
				this.refresh = true;
				if(this.fromPage=='myFollows'){
					getFollowedUser(uni.getStorageSync('userId'),this.page,8).then(res=>{
						this.userList = res;
						this.refresh = false;
					})
				}else if(this.fromPage=='myFans'){
					getFansUser(uni.getStorageSync('userId'),this.page,8).then(res=>{
						this.userList = res;
						this.refresh = false;
					})
				}else{
					recommendUser(uni.getStorageSync('userId'),this.page,8).then(res => {
						this.userList = res;
						this.refresh = false;
					})
					return Promise.resolve(true)
				}
				
			},
			
			
	  }
	}
</script>

<style lang='scss' >
	.userAll{
		height: 100%;
		.itemContainer{
			width: 100%;
			display: flex;
			flex-direction: column;
			align-items: center;
			.friend-item {
				width: 93%;
				display: flex;
				justify-content: center;
				padding: 10px 10px;
				border: 1px solid #ECECEC;
				border-radius: 3px;
				margin-bottom: 30rpx;
				box-shadow: 0px 2rpx 8rpx 0px rgba(165, 165, 165, 0.5);
				&:nth-of-type(3n) {
					margin-right: 0;
				}
			    
				
			
			}
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