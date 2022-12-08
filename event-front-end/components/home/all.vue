<template>
	<view class="all">
		<scroll-view scroll-y class="all" :refresher-enabled="fromPage=='home'?true:false" enable-back-to-top lower-threshold=250 @scrolltolower="reachBottom()" :refresher-triggered="refresh" @refresherrefresh="pullDownRefresh()" >
			<view>
				<view v-if="!show">
					<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
						<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
							title loading avatar></u-skeleton>
					</view>
				</view>
				<view v-else>
					<view v-if="lists.length!=0" class="post">
						<view v-for="(item,index) in lists" :key="index" class="listItem">
							<!-- <view class="listItem"> -->
								<!-- <view v-for="(post,postIndex) in item" v-show="postIndex<3">
									<postItem :postInfo='post'></postItem>
								</view>
								<view v-for="(team,teamIndex) in teams" style="margin-top: 30rpx;" v-show="teamIndex>=3">
									<teamItem :teamData="team"></teamItem>
								</view> -->
								<view v-if="item.postId!=''&&item.postId!=null">
									<postItem :postInfo='item'></postItem>
								</view>
								<view v-if="item.teamId!=''&&item.teamId!=null" style="margin-top: 30rpx;">
									<teamItem :teamData="item"></teamItem>
								</view>
							<!-- </view> -->
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
import postItem from 'components/post/post-item'
import teamItem from '@/components/home/team-item.vue'
import { getPostByUptime,hasLikePost,getPostBySearch,getFollowPost,getNotFinishedTeam,getPostByContest,getTeamByContest,getTeamBySearch } from '@/common/API.js'
export default {
	components: { postItem,teamItem},
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
		lists:[],
		posts:[],
		teams:[],
		page:1,
		noMore:false, 
		refresh:false
  }),
  mounted(){
	  if(this.fromPage=='contestDetail'){
		  getPostByContest(this.keyWord,this.page,3).then(res =>{
			this.lists=this.lists.concat(res);
			this.show = true
			this.refresh = false;
			getTeamByContest(this.keyWord,this.page,2).then(rs => {
			  this.lists= this.lists.concat(rs)
			  this.teams = rs
			  this.show = true
			  this.refresh = false
			},err=>{console.log(err)})
		  },err=>{console.log(err)})	
		}
	  else if(this.fromPage=='searchDetail'){
		  const p1={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 3}
		  getPostBySearch(p1).then(res =>{
			  this.lists=this.lists.concat(res);
			  this.show = true
			  this.refresh = false;
			  const p2={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 2}
			  getTeamBySearch(p2).then(rs => {
					this.lists= this.lists.concat(rs)
					this.teams = rs
					this.show = true
					this.refresh = false
			  },err=>{console.log(err)})
		  },err=>{console.log(err)})
	  }else{
		  var itemList=[];
		    const p={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 3}
		    getPostBySearch(p).then(res =>{
		  	this.lists=this.lists.concat(res);
		  	this.show = true
		  	this.refresh = false;
		  	getNotFinishedTeam(2,this.page-1).then(rs => {
		  	  this.lists= this.lists.concat(rs)
		  	  this.teams = rs
		  	  this.show = true
		  	  this.refresh = false
		  	},err=>{console.log(err)})
		    },err=>{console.log(err)})
	}
  },
  methods:{
	  getMoreData(){
	    this.page++;
		if(this.fromPage=='contestDetail'){
			getPostByContest(this.keyWord,this.page,3).then(res =>{
				var len=this.lists.length;
				this.lists=this.lists.concat(res);
				getTeamByContest(this.keyWord,this.page,2).then(rs => {
				  this.lists= this.lists.concat(rs)
				  if(this.lists.length==len) this.noMore=true;
				},err=>{console.log(err)})
			},err=>{console.log(err)})
		}else if(this.fromPage=='searchDetail'){
			const p1={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 3}
			getPostBySearch(p1).then(res =>{
				  var len=this.lists.length;
				  this.lists=this.lists.concat(res);
				  const p2={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 2}
				  getTeamBySearch(p2).then(rs => {
						this.lists= this.lists.concat(rs)
						if(this.lists.length==len) this.noMore=true;
				  },err=>{console.log(err)})
			},err=>{console.log(err)})
		}else{
			const p={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 3}
			getPostBySearch(p).then(res =>{
				var len=this.lists.length;
				this.lists=this.lists.concat(res);
				getNotFinishedTeam(2,this.page-1).then(rs => {
				  this.lists= this.lists.concat(rs)
				  if(this.lists.length==len) this.noMore=true;
				},err=>{console.log(err)})
			},err=>{console.log(err)})
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
			if(this.fromPage=='contestDetail'){
				getPostByContest(this.keyWord,this.page,3).then(res =>{
					var len=this.lists.length;
					this.lists=this.lists.concat(res);
					getTeamByContest(this.keyWord,this.page,2).then(rs => {
					  this.lists= this.lists.concat(rs)
					  this.refresh=false
					},err=>{console.log(err)})
				},err=>{console.log(err)})
			}else if(this.fromPage=='searchDetail'){
				const p1={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 3}
				getPostBySearch(p1).then(res =>{
					  var len=this.lists.length;
					  this.lists=this.lists.concat(res);
					  const p2={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 2}
					  getTeamBySearch(p2).then(rs => {
							this.lists= this.lists.concat(rs)
							this.refresh=false
					  },err=>{console.log(err)})
				},err=>{console.log(err)})
			}else{
				const p={"keyword": this.keyWord,"pageNum": this.page,"pageSize": 3}
				getPostBySearch(p).then(res =>{
					var len=this.lists.length;
					var arr=this.lists.concat(res);
					getNotFinishedTeam(2,this.page-1).then(rs => {
					  this.lists= arr.concat(rs)
					  this.refresh=false
					},err=>{console.log(err)})
				},err=>{console.log(err)})
				
			}
			
		},
  }
}
</script>

<style lang='scss'>
	.all{
		height: 100%;
		.post{
			height: 100%;
			width: 100%;	
			padding-bottom: 30rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			align-items: center;
			.listItem{
				width: 100%;
				height: 100%;
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