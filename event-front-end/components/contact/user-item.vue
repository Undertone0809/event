<template>
  <view  class="container">
  	<div class="left" @click="goUser(item)">
  		<image
  			:src="item.avatar"
  			class="avatar" />
  		<div class="info">
  			<div class="site-name">{{item.openname}}</div>
  			<div class="line" >
  				<!-- <div class="site-detail" v-if="formatString1(item.userEdu)">{{item.userEdu}}</div>
  				<div class="site-detail">{{formatString2(item.userSkill)}}</div>
  				<div class="site-detail">{{formatString2(item.userPrize)}}</div> -->
  				<view v-if="formatString1(item.userEdu)"  style="padding: 5rpx;">
  					<u-tag v-if="formatString1(item.userEdu)" @click="goUser(item)" :text="item.userEdu" size="mini" plainFill icon="integral" plain ></u-tag>
  				</view>
  				<view v-if="formatString2(item.userSkill)!=null" style="padding: 5rpx;">
  					<u-tag v-if="formatString2(item.userSkill)!=''" @click="goUser(item)" :text="formatString2(item.userSkill)" plainFill type="error" size="mini" plain></u-tag>
  				</view>
  				<view v-if="formatString2(item.userPrize)!=null" style="padding: 5rpx;">
  					<u-tag v-if="formatString2(item.userPrize)!=''" @click="goUser(item)" :text="formatString2(item.userPrize)" plainFill type="error" size="mini" plain></u-tag>
  				</view>
  				
  			</div>
  		</div>
  	</div>
  	<div class="right" >
  		<!-- 关注 -->
  		<view v-if="item.userid!=userId" @click="toAttention(item.userid)" class="attention" :style="{'background-color': attentionColor}">
  		  {{attentionSet}}
  		</view>
  		<!-- 这个css代码对flex的优化不太行，浪费一行代码写一个空的占位符出来 -->
  		<view v-else class="attention"></view>									
  	</div>
  </view>
</template>

<script>
import { FRESH_POST_PAGE } from 'common/topic_enum.js'
import { likePost,dislikePost,hasLikePost,followHim,unfollowHim } from '@/common/API.js'
export default {
  props: {
    item: {
      type: Object,
      default: []
    }
  },
  data: () => ({
		attentionSet:'',
		attentionColor:'',
		userId:''
  }),
  mounted(){
	this.userId=uni.getStorageSync("userId")
	if(this.item.hasFollow==true){
		this.attentionSet="已关注";
		this.attentionColor="#999";
	}else{
		this.attentionSet="+关注";
		this.attentionColor="#dd524d";
	}
  },
  methods:{
	  goUser(item) {
	  	if(item) uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+item.userid})
	  	else uni.$u.toast('该用户无id')
	  },
	  formatString1(e){
	  	var str = e;
	  	if(str.lastIndexOf("大学")>1||str.lastIndexOf("学院")>1)
	  		return true  // true
	  	else
	  		return false
	  },
	  formatString2(e){
	  	var str = e;
	  	var arr= str.split("^");
	  	return arr[1]
	  },
	  //用户关注
	  toAttention(e){
	  	if(!uni.getStorageSync('hasLogin')){
	  	  uni.showToast({title:'请在登录后进行操作',icon:'none'})
	  	  return
	  	}
	  	let myid = uni.getStorageSync('userId')
	  	if(!myid) return
	  	if(this.item.hasFollow==false)
	  	{
	  		followHim(myid,e).then(res=>{
	  			this.attentionSet="已关注";
				this.attentionColor="#999";
	  			uni.showToast({
	  				title:'关注成功',
	  				duration:2000
	  			});
	  			this.isAttention=true;
	  			
	  		},err=>{
	  			console.log(err)
	  		})
	  		
	  	}
	  	else{
	  		unfollowHim(myid,e).then(res=>{
	  			this.attentionSet="+关注";
				this.attentionColor="#dd524d";
	  			uni.showToast({
	  				title:'取消关注成功',
	  				duration:2000
	  			});
	  			this.isAttention=false;
	  			
	  		},err=>{
	  			console.log(err)
	  		})
	  		
	  	}
	  },
}
		
}
</script>

<style lang='scss'>
	.container{
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 100%;
		.left {
			display: flex;
			justify-content: space-between;
			align-items: center;
			.avatar {
				width: 100rpx;
				height: 100rpx;
				margin-right: 40rpx;
				border-radius: 50%;
			}
			.info{
				.site-name {
					border-bottom: 1px dotted #ECECEC;
					background-image: linear-gradient(-3deg, #ffffff 0%, #ff6464 100%);
					font-size: 34rpx;
					-webkit-background-clip: text;
					color: #636363;
					font-weight: 100;
					//font-family: monospace;
					width: 300rpx;
					white-space: nowrap;
					text-overflow: ellipsis;
					overflow: hidden;
				}
				.line{
					width: 330rpx;
					display: flex;
					flex-wrap: wrap;
					color: #636363;
					font-size: 13px;
					padding-top: 10rpx;
					.site-detail {
						overflow: hidden;
	
					}
			}
		}
		}			
		.right {
			display: inline-block;
			margin-left: 30rpx;
			.attention{
				width: 100rpx;
				height: 50rpx;
				border-radius:8rpx;
				color: #FFFFFF;
				font-size: 26rpx;
				display: flex;
				justify-content: center;
				align-items: center;
			}
			.line{
				display: flex;
				align-items: center;
				color: #636363;
				font-size: 13px;
				.site-detail {
					// white-space: nowrap;
					// text-overflow: ellipsis;
					overflow: hidden;
					//padding-bottom: 5rpx;
					line-height: 1.5rem;
					//padding-top: 5rpx;
				}
			}
			
					
			
		}
	}
</style>