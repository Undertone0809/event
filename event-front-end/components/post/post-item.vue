<template>
  <view :class="fromPage=='user'?'postOuter2':'postOuter1'" >
    <view class="postItem" @click="goDetail()">
      <view class="user" >
        <image class="userHead" :src="postInfo.avatar" mode="aspectFill" @click.stop="goUser()"/>
        <view @click.stop="goUser()">
          <view class="userName">{{postInfo.openname}}</view>
          <view class="userLocation" v-if="province">{{`IP属地:${province}`}} </view>
        </view>
        
      </view>
	  <view class="tag" style="display: flex;align-items: center; padding: 10rpx 0;" v-if="postInfo.userEdu!==''&&postInfo.userEdu==userEdu&&postInfo.userId!==userId||postInfo.contestTitle!=null">
	  	<view class="tagItem">
	  		<view  v-if="postInfo.userEdu!==''&&postInfo.userEdu==userEdu&&postInfo.userId!==userId" style="margin-right: 10rpx;">
	  			<u-tag text="与我同校" plainFill type="warning" size="mini" shape="circle" plain></u-tag>
	  		</view>
	  		<view  v-if="postInfo.contestTitle!=null">
	  			<u-tag :text="'#'+postInfo.contestTitle" plainFill color="#276bf4" shape="circle" size="mini" plain @click="toContest(postInfo.contestId)"></u-tag>
	  		</view>
	  	</view>
	  </view>
      <view class="content">
        <view class="text">
          <view class="textInfo">
            {{postInfo.postContent}}
          </view>
        </view>
      </view>
      <view class="album" v-if="postInfo.postImgUrl">
        <u-album :urls="postInfo.postImgUrl?postInfo.postImgUrl.split(' ') : []" singleSize="280" multipleSize="28vw" maxCount="3" space="2" ></u-album>
      </view>
    </view>
    <view :class="fromPage=='user'?'information2':'information1' ">
      <view v-if="false" class="time">
        {{new Date(postInfo.postUptime).valueOf() | timeFrom }}
      </view>
	  <view class="like">
	    <view class="iconItem">
	      <image class="iconStyle" src="../../static/post/fire.png"/>
	      <text style="">{{postInfo.postViews}}</text> 
	    </view>
		  <view class="iconItem">
			  <image class="iconStyle" src="../../static/post/comment.png" />
			  <text style="">{{postInfo.postCommentNum}}</text>
		  </view>
		  <view class="iconItem"  @click="like()">
			<image class="iconStyle" :src="hasLike==true?'../../static/post/like-fill.png':'../../static/post/like.png'"/>
			<text style="">{{likeNum}}</text> 
		  </view>
	  </view>
    </view>
  </view>
</template>

<script>
import { FRESH_POST_PAGE } from 'common/topic_enum.js'
import { likePost,dislikePost,hasLikePost,getUserIntro,getContestById } from '@/common/API.js'
export default {
  props: {
    postInfo: {
      type: Object,
      default: []
    },
	fromPage:{
		type: String,
		default(){ return '' }
	}
  },
  watch:{
    province(newVal,oldVal){
      // console.log(newVal,oldVal)
    }
  },
  data: () => ({
    province:'',
	hasLike:'',
	likeNum:1,
	userEdu:'',
	userId:''
  }),
  mounted(){
	  this.userId=uni.getStorageSync('userId');
	  if(this.userId){
		  getUserIntro(uni.getStorageSync('userId')).then(rs => {this.userEdu=rs.userEdu})
	  }
    uni.request({
      url: `https://ip.useragentinfo.com/json?ip=${this.postInfo.postIp}`,
    }).then(res => {this.province = res[1].data.province })
	
	this.likeNum=this.postInfo.postLikeNum;
	// console.log(this.postInfo)
  },
  methods:{
		goDetail(){ 
      this.postInfo.postViews++
      // this.$disp.fire(FRESH_POST_PAGE)
	  console.log(this.postInfo)
	  if(this.fromPage!='user'){
		  uni.navigateTo({url:'/subPackageA/pages/post/detail?postId='+this.postInfo.postId+'&commentNum='+this.postInfo.postCommentNum})
	  }
       
    },
	like(){
		if(!uni.getStorageSync('hasLogin')){
		  uni.showToast({title:'请在登录后进行操作',icon:'none'})
		  return
		}
		if(this.hasLike){
			likePost(uni.getStorageSync('userId'),this.postInfo.postId).then(res=>{
				console.log(res)
				// uni.$u.toast("取消点赞成功")
			},err=>{
				console.log(err)
				// uni.$u.toast("取消点赞失败")
			})
			this.hasLike=false;
			this.likeNum--;
		}else{
			console.log(this.postInfo)
			likePost(uni.getStorageSync('userId'),this.postInfo.postId).then(res=>{
				console.log(res)
				// uni.$u.toast("点赞成功")
			},err=>{
				console.log(err)
				// uni.$u.toast("点赞失败")
			})
			this.hasLike=true;
			this.likeNum++;
		}
	},
	toContest(e){
		console.log(1)
		getContestById(e).then(res=>{
			let contestItem=encodeURIComponent(JSON.stringify(res))
			uni.navigateTo({url:`/subPackageA/pages/home/contestDetail?contestItem=`+contestItem})
		})
		
	},
	goUser() {
		if(this.fromPage!='user'){
			uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+this.postInfo.userId})
		}
		
	},
  }
}
</script>

<style lang='scss'>
	.postOuter1{
		width: 93%;
		overflow: hidden;
		padding: 25rpx;
		background-color:#FFFFFF;
		margin: 30rpx auto 0 auto;
		border-radius: 10rpx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		box-shadow: 0px 2rpx 8rpx 0px rgba(165, 165, 165, 0.5);
		.postItem{
			width: 700rpx;
		}
	}
	.postOuter2{
		width: 85%;
		background-color: pink;
		overflow: hidden;
		padding: 25rpx;
		background-color:#FFFFFF;
		margin: 0 auto 0 auto;
		border-radius: 10rpx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		.postItem{
			width: 650rpx;
		}
	}
	.user{
		display: flex;
		align-items: center;
		padding: 0 23rpx;
		
    // border: 2rpx solid #000;

		.userHead{
			width: 75rpx;
			height: 75rpx;
			border-radius: 50%;
			background-size: contain;
			margin-right: 15rpx;
		}
		.userName{
			font-weight: bold;
			font-size: 28rpx;
			color: #585858;
		}
    .userLocation{
      font-size: 25rpx;
    }
		.userEmail{
			color: rgb(153, 153, 153);
			font-size: 26rpx;
			
		}
	}

	.tag{
		width: 100%;
		
		/* display: flex;
		flex-direction: column;
		justify-content: center;
		background-color: pink; */
		.tagItem{
			padding: 0 24rpx;
			display: flex;
		}
	}
	.title{
		width: 100%;
		padding: 24rpx 23rpx;
		padding-top: 20rpx;
		display: flex;
		align-items: center;
		font-size: 34rpx;
		font-weight: bold;
		
	}
	.content{
		width: 100%;
		font-size: 28rpx;
		padding: 0 23rpx;
		/* padding-top: 12rpx; */
		.text{
			.textInfo{
				width: 100%;
				padding: 7rpx 0;
				overflow:hidden;  
				text-overflow: ellipsis;
				word-break: break-all;
				display: -webkit-box;
				-webkit-box-orient:vertical;
				-webkit-line-clamp: 2;
				color: #616161;
			}
		}
	}
	.information1{
		width: 700rpx;
		height: 50rpx;
		display: flex;
		justify-content: space-between;
		padding: 0 23rpx;
		padding-top: 10rpx;
		view{
			height: 50rpx;
			color: #888;
			display: flex;
			align-items: center;
			font-size: 26rpx;
		}
		.like{
			display: flex;
			justify-content: space-between;
			align-items: center;
			width: 230rpx;
			font-size: 30rpx;
			color: black;
			margin-left: -6rpx;
			.iconStyle{
				width: 32rpx;
				height: 32rpx;
				margin: 0 10rpx;
			}
		}
		
		
	}
	.information2{
		width: 700rpx;
		height: 50rpx;
		display: flex;
		justify-content: space-between;
		padding: 0 50rpx;
		padding-top: 10rpx;
		view{
			height: 50rpx;
			color: #888;
			display: flex;
			align-items: center;
			font-size: 26rpx;
		}
		.like{
			display: flex;
			justify-content: space-between;
			align-items: center;
			width: 230rpx;
			font-size: 30rpx;
			color: black;
			margin-left: -6rpx;
			.iconStyle{
				width: 32rpx;
				height: 32rpx;
				margin: 0 10rpx;
			}
		}
		
		
	}
	.album {
		width: 100%;
		padding: 0 23rpx;
		margin-top: 12rpx;
		margin-bottom: 9rpx;
		display: flex;
		justify-content: space-between;
	}
</style>