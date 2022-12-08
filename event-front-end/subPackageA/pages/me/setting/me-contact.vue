<template>
	<view class="container">
	  <view class="topInfoBar">
	    <image class="topInfoBar_logo" src="/static/me/一问社区logo.png"></image>
	    <view class="topInfoBar_title">一问Event</view>
		<view class="topInfoBar_detail">公测0.2.7(220224)</view>
		<view class="topInfoBar_detail">欢迎加入一问Event交流群~</view>
		<view class="topInfoBar_detail">QQ群号:996975155</view>
	    <view class="topInfoBar_version">{{version}}</view>
	  </view>
	  <view class="midWrap">
	    <view class="aboutBox">
	      <view class="aboutBox_title"></view>
	      <view class="aboutBox_detail"></view>
	    </view>
	  </view>
	  
	  <view class="subBtn" @click="copyWords">复制群号</view>

	  <button class="contactBtn" open-type="contact" @bindcontact='contactUs'> 联系客服</button>
	
	  <view class="bottomWrap">© 2021-NOW mygirlfriends.cn All rights reserved.</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		onLoad() {

		},
		methods: {
			copyWords(){
				uni.setClipboardData({
				    data: '996975155',
					success: function (){
						uni.showToast({
						    title: '复制成功',
						    duration: 2000
						});
					}
				});
				
			},
			contactUs(e){
				console.log(e);
			}
		}
	}
</script>

<style>
	.topInfoBar{
	  text-align: center;
	  margin-top: 25rpx;
	}
	.topInfoBar_logo{
	  width: 248rpx;
	  height: 200rpx;
	}
	.topInfoBar_title{
	  font-size: 35rpx;
	  font-weight: 800;
	  color: #454545;
	}
	.topInfoBar_detail{
	  margin-top: 10rpx;
	  font-size: 28rpx;
	  font-weight: 700;
	  color: #666666;
	}
	.topInfoBar_version{
	  margin-top: 12rpx;
	  font-size: 24rpx;
	  color: rgba(0, 0, 0, 0.6);
	}
	.midWrap{
	  margin-top: 50rpx;
	}
	.aboutBox{
	  padding-left: 30rpx;
	}
	.aboutBox_title{
	  font-weight: 700;
	  color: rgba(0, 0, 0, 0.8);
	  font-size: 30rpx;
	}
	.aboutBox_detail{
	  margin-top: 16rpx;
	  color: rgba(0, 0, 0, 0.6);
	  font-size: 26rpx;
	}
	.subBtn{
	  text-align: center;
	  width: 44%;
	  margin: 0 auto;
	  padding: 25rpx 0;
	  border-radius: 50rpx;
	  font-size: 28rpx;
	  font-weight: 700;
	  color: #9b9b9b;
	  background: rgba(0,0,0,0.05);
	  letter-spacing: 1rpx;
	}
	.contactBtn{
		margin: 0 auto;
		margin-top: 20rpx;
		width: 44%;
		height: 84rpx;
		font-size: 28rpx;
		border-radius: 50rpx;
		line-height: 84rpx;
		background-color: #4dadff;
		color: #fff;
	}
	.bottomWrap{
	  width: 100%;
	  position: absolute;
	  text-align: center;
	  bottom: 32rpx;
	  font-size: 24rpx;
	  font-weight: 700;
	  color: rgba(0, 0, 0, 0.5);
	}
</style>
