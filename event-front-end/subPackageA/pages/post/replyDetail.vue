<template>
<view v-if="!show">
	<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
		<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
			title loading avatar></u-skeleton>
	</view>
</view>
<view v-else class="page-all">
	<view class="top">
		<!-- 评论用户信息 -->
		<view class="cuser" @click="isMyPost()">
			<view class="cuserIntro">
				<image :src="detail.avatar" mode="aspectFill" class="cuserHead"></image>
				<view class="cuserInfo" @click="toReplyFirst()">
					<view class="cuserName">{{detail.openname}}</view>
				</view>
			</view>
			<view>
				<image class="more" src="../../static/post/more.png" @click="toShowMorePost()">
				<view v-if="canRemovePost">
					<u-action-sheet :actions="RemoveList" :title="title" :show="showMorePost" :closeOnClickOverlay="true" 
					@select="selectClickPost()" round=15 @close="close()"></u-action-sheet>
				</view>
				<view v-if="!canRemovePost">
					<u-action-sheet :actions="NotRemoveList" :title="title" :show="showMorePost" :closeOnClickOverlay="true" 
					@select="selectClickPost()" round=15 @close="close()"></u-action-sheet>
				</view>
				<view v-if="canRemoveComment">
					<u-action-sheet :actions="RemoveList" :title="title" :show="showMoreComment" :closeOnClickOverlay="true" 
					@select="selectClickPost()" round=15 @close="close()"></u-action-sheet>
				</view>
				<view v-if="!canRemoveComment">
					<u-action-sheet :actions="NotRemoveList" :title="title" :show="showMoreComment" :closeOnClickOverlay="true" 
					@select="selectClickPost()" round=15 @close="close()"></u-action-sheet>
				</view>
			</view>
		</view>
		<!-- 评论内容 -->
		<view class="ccontent" @click="toReplyFirst()">
			{{detail.commentContent}}
		</view>
		<!-- 评论基本信息 -->
		<view class="commentInfo ">
			<!-- 评论时间 -->
			<view class="commentTime" @click="toReplyFirst()">
				{{new Date(detail.commentTime).valueOf() | timeFrom }}
			</view>
			<!-- 评论的点赞和回复 -->
			<view class="commentDetail">
				<image class="commentTwice" src="../../static/post/comment.png" @click="toReplyFirst()"></image>
				<!-- <image class="commentLike" src="/static/home/like.png" @click="like()" v-if="detail.isLike==false"></image>
				<image class="commentLike" src="/static/home/like-fill.png" @click="like()" v-else></image>
				<text @click="toReplyFirst()">{{detail.like}}</text> -->
			</view>
		</view>
	</view>
		<view class="middle">
				<view class="replyNumber">
					共{{detail.reply.length}}条回复
				</view>
				<!-- 二级评论单位楼层 -->
				<view class="reply" v-for="(replyItem,index) in replyList" :key="replyItem.commentId">
					<!-- 二级评论用户信息 -->
					<view class="cuser" @click="isMyComment(replyItem)">
						<view class="cuserIntro">
							<image :src="replyItem.avatar" mode="aspectFill" class="replyUserHead"></image>
							<view class="cuserInfo">
								<view class="replyUserName">{{replyItem.openname}}</view>
								<view class="replyUserName" v-if="replyItem.replyName"> 
									<image src="../../static/post/to.png" style="width: 20rpx;height: 20rpx;margin: 0 10rpx;"></image> 
									<text style="font-weight: bold;">{{replyItem.replyName}}</text>
								</view>
							</view>
						</view>
						<image class="more" src="../../static/post/more.png" @click="toShowMoreComment()">
					</view>
					<!-- 二级评论内容 -->
					<view class="replyContent" @click="toReplySecond(replyItem,index)">
						{{replyItem.commentContent}}
					</view>
					<!-- 二级评论基本信息 -->
					<view class="commentInfo replyInfo">
						<!-- 二级评论时间 -->
						<view class="commentTime" @click="toReplySecond(replyItem,index)">
							{{new Date(replyItem.commentTime).valueOf() | timeFrom }}
						</view>
						<!-- 二级评论的点赞和回复 -->
						<view class="commentDetail">
							<image class="commentTwice" src="/static/post/comment.png" @click="toReplySecond(replyItem,index)"></image>
<!-- 							<image class="commentLike" src="/static/home/like.png" @click="replyLike(index,replyIndex)" v-if="replyItem.isLike==false"></image>
							<image class="commentLike" src="/static/home/like-fill.png" @click="replyLike(index,replyIndex)" v-else></image> -->
							<!-- <text @click="toReplySecond(replyItem,index)">{{replyItem.commendLikes}}</text> -->
						</view>
					</view>
				</view>
				<view class="commentBottomBar">
					---这是我的底线---
				</view>
			</view>
			<!-- 底部输入栏(从chat.vue粘贴修改的...) -->
				<sendInput ref="sendInput" v-on:send_replyDetail="send()" :holdWord="holdWord" inPage="replyDetail"></sendInput>
			</view>
	
	
</template>

<script>
import {getSubAll,sendComment,getComment,removeComment} from '@/common/API.js'
import sendInput from "../../components/common/sendInput.vue"
export default {
	components: { sendInput },
	data() {
		return {
			commentId:'',
			show:false,
			detail:{},
			replyList:[],
			rpitem:{},
			holdWord:"发表评论...",//textarea中的文字,回复他人时会改变
			toUser:"",//在二级评论中回复的对象的用户名
			replyType:"first",//first 在评论区中的评论里建立一个二级评论 3.second 回复一个二级评论 
			showMorePost:false,//展示一级评论的更多选项变量
			showMoreComment:false,//展示二级评论的更多选项变量
			RemoveList: [
					{
						name:'删除',
						color:'#dd524d',
						fontSize:'20'
					},
					{
						name: '分享',
						disabled:true
					}
				],
			NotRemoveList: [
					{
						name:'举报',
						color:'#dd524d',
						fontSize:'20'
					},
					{
						name: '分享',
						disabled:true
					}
				],
			postId:'',
			canRemovePost:false,
			canRemoveComment:false,
			onSelectCommentId:'',
			onSelectType:""
		}
			
	},
	async onLoad(option) {
		this.commentId=option.commentId
		if(option.commentId){
			let p0 = getComment({commentId:option.commentId})
			let p1 = getSubAll(option.commentId)
			Promise.all([p0,p1]).then(rs => {
				this.detail = rs[0][0]
				this.detail.reply = rs[1]
				this.detail.reply.sort((a,b) => b.commentTime-a.commentTime)
				this.holdWord="回复 "+this.detail.openname;
				this.show = true
				this.replyList=this.detail.reply
			})
		}
		
	},
	onShow() {
		uni.$on("freshDetail",res => {
			if(this.commentId){
				let p0 = getComment({commentId:this.commentId})
				let p1 = getSubAll(this.commentId)
				Promise.all([p0,p1]).then(rs => {
					this.detail = rs[0][0]
					this.detail.reply = rs[1]
					this.detail.reply.sort((a,b) => b.commentTime-a.commentTime)
					this.holdWord="回复 "+this.detail.openname;
					this.show = true
					this.replyList=this.detail.reply
				}).then(()=>{
						uni.$u.toast('回复成功')
						console.log("这里刷新")
					})
			}
			
		})
		uni.onKeyboardHeightChange(res => {
			const windowWidth = this.getWindowWidth()
      // 键盘防抖
      // 监听软键盘的高度 
			// 当点击软键盘自带的收起按钮的时候也就是会隐藏软键盘 这时候监听到的软键盘的高度就是0
			// 让输入框取消焦点 这时候再去输入内容的时候 输入框就会弹起
      uni.$u.throttle(()=>{ this.keyHeight=res.height * (750 / windowWidth) },20)
			console.log("软键盘高度："+this.keyHeight)
		})
	},
	methods: {
		
		// 点赞一级评论方法
		// like(){
		// 	if(this.detail.isLike==false)
		// 	{
		// 		this.detail.isLike=true
		// 		this.detail.like=this.detail.like+1;
		// 	}
		// 	else
		// 	{
		// 		this.detail.isLike=false
		// 		this.detail.like--;
		// 	}
			
		// },
		// // 点赞二级评论方法
		// replyLike(index){
		// 	if(this.detail.reply[index].isLike==false)
		// 	{
		// 		this.detail.reply[index].isLike=true
		// 		this.detail.reply[index].like=this.detail.reply[index].like+1;
		// 	}
		// 	else
		// 	{
		// 		this.detail.reply[index].isLike=false
		// 		this.detail.reply[index].like=this.detail.reply[index].like-1;
		// 	}
			
		// },
		// 点击一级评论弹出软键盘且显示回复对象方法
		toReplyFirst(){
			this.$refs.sendInput.isFocus=true;
			this.replyType="first";
			this.holdWord="回复 "+this.detail.openname;
		},
		// 点击二级评论弹出软键盘且显示回复对象方法
		toReplySecond(item,index){
			this.$refs.sendInput.isFocus=true;
			this.rpitem=item;
			this.replyType="second";
			this.toUser=item.openname;
			this.holdWord="回复 "+item.openname;
		},
		send(textMsg){
				uni.showLoading({title:'请稍后'})
				let me = uni.getStorageSync('userInfo')
				let it = {
					commentId:"comment"+this.detail.reply.length,
					avatar:me.avatar,
					openname:me.openname,
					commentContent:textMsg,
					commendLikes:0,
					commentTime:new Date(),
				}
				if(this.replyType == 'first') {
					let t = {commentContent:it.commentContent, postId:this.detail.postId, userId:me.userId,faCommentId:this.detail.commentId}
					sendComment(t).then(() => {
						if(!this.comment) this.comment = []
						if(this.commentId){
							let p0 = getComment({commentId:this.commentId})
							let p1 = getSubAll(this.commentId)
							Promise.all([p0,p1]).then(rs => {
								this.detail = rs[0][0]
								this.detail.reply = rs[1]
								this.detail.reply.sort((a,b) => b.commentTime-a.commentTime)
								this.holdWord="回复 "+this.detail.openname;
								this.show = true
								this.replyList=this.detail.reply
							}).then(()=>{
								uni.$u.toast('回复成功')
								uni.$emit("freshComment",null)
							})
						}
						uni.hideLoading()
					}).catch(()=>{
						uni.$emit("freshDetail",null)
					})
				} else {
					let t = {commentContent:it.commentContent, postId:this.detail.postId, userId:me.userId,faCommentId:this.rpitem.commentId}
					sendComment(t).then(() => {
						it.replyName = this.rpitem.openname
						if(this.commentId){
							let p0 = getComment({commentId:this.commentId})
							let p1 = getSubAll(this.commentId)
							Promise.all([p0,p1]).then(rs => {
								this.detail = rs[0][0]
								this.detail.reply = rs[1]
								this.detail.reply.sort((a,b) => b.commentTime-a.commentTime)
								this.holdWord="回复 "+this.detail.openname;
								this.show = true
								this.replyList=this.detail.reply
							}).then(()=>{
								uni.$u.toast('回复成功')
								uni.$emit("freshDetail",null)
							})
						}
						this.holdWord="回复 "+this.detail.openname;
						this.replyType="first";
						uni.hideLoading()
					}).catch(()=>{
						uni.$emit("freshDetail",null)
					})
					
				}
		},
		toShowMorePost(){
			this.showMorePost=true;
			this.onSelectType="Post";
		},
		toShowMoreComment(){
			this.showMoreComment=true;
			this.onSelectType="Comment";
		},
		close(){
			this.showMorePost=false;
			this.showMoreComment=false;
			this.onSelectType="";
		},
		selectClickPost(index){
			if(index.name=="删除"&&this.canRemovePost==true&&this.onSelectType=="Post"){
				uni.showModal({
					title:'提示',
					content:'确认删除该评论吗？',
					success: (res) => {
						if (res.confirm){
							removeComment(this.detail.commentId);
							this.$store.commit("setFresh", true)
							uni.$emit("freshComment",this.detail.commentId)
							uni.navigateBack({
								delta:1
							})
							uni.$u.toast("删除评论成功")
						}   
					}
				})
			}
			if(index.name=="删除"&&this.canRemoveComment==true&&this.onSelectType=="Comment"){
				uni.showModal({
					title:'提示',
					content:'确认删除该评论吗？',
					success: (res) => {
						if (res.confirm){
							removeComment(this.onSelectCommentId);
							var that=this;
							this.replyList.forEach(function(item, index, arr) {
							    if(item.commentId == that.onSelectCommentId) {
							        arr.splice(index, 1);
							    }				
							})
							uni.$emit("freshComment",this.onSelectCommentId)
							uni.$emit("freshComment",null)
							uni.$u.toast("删除评论成功")
						}
					}
				})
			}
		},
		isMyPost(){
			if(this.detail.userId==uni.getStorageSync('userId'))
				{
					this.canRemovePost=true;
				}
			else
				{
					this.canRemoveComment=false;
				}
		},
		isMyComment(item){
			if(item.userId==uni.getStorageSync('userId'))
				{
					this.canRemoveComment=true;
					this.onSelectCommentId=item.commentId;
				}
			else
				{
					this.canRemoveComment=false;
					this.onSelectCommentId=item.commentId
				}
				
		}
	}
}
</script>

<style lang="scss" scoped>
	.top{
		padding: 20rpx 20rpx;
		padding-bottom: 0;
		box-shadow: 0px 2rpx 8rpx 0px rgba(165, 165, 165, 0.5);
	}
	.cuser{
		width: 100%;
		height: 60rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		font-size: 30rpx;
		.cuserIntro{
			display: flex;
			align-items: center;
			.cuserHead{
				width: 60rpx;
				height: 60rpx;
				border-radius: 30rpx;
				background-size: contain;
				margin-right: 30rpx;
			}
			.cuserName{
				font-weight: bold;
			}
			.cuserInfo{
				display: flex;
				align-items: center;
			}
		}
		.more{
			width: 40rpx;
			height: 40rpx;	
			margin-right: 32rpx;
		}
		
	}
	.ccontent{
		text-align: justify;
		padding-left: 90rpx;
		padding-right: 20rpx;
		padding-top: 4rpx;
		padding-bottom: 10rpx;
		font-size: 28rpx;
	}
	.commentInfo{
		display: flex;
		justify-content: space-between;
		padding-left: 90rpx;
		padding-right: 20rpx;
		color: rgb(153, 153, 153);
		font-size: 26rpx;
		padding-bottom: 30rpx;
		.commentDetail{
			display: flex;
			align-items: center;
		}
		.commentTwice{
			width: 35rpx;
			height: 35rpx;
			margin-right: 15rpx;
		}
		.commentLike{
			width: 35rpx;
			height: 35rpx;
		}
	}
	.middle{
		.replyNumber{
			font-weight: bold;
			padding: 25rpx 25rpx;
		}
		.reply{
			padding: 0 20rpx;
			.replyUserHead{
				width: 50rpx;
				height: 50rpx;
				border-radius: 50%;
				background-size: contain;
				margin-right: 15rpx;
			}
			.replyUserName{
				font-size: 28rpx;
				font-weight: bold;
			}
			.replyContent{
				text-align: justify;
				padding-left: 65rpx;
				padding-right: 20rpx;
				padding-top: 4rpx;
				padding-bottom: 10rpx;
				font-size: 28rpx;
			}
			.replyInfo{
				
				padding-left: 65rpx !important;
				
			}
			.more{
				width: 40rpx;
				height: 40rpx;
				margin-right: 32rpx;
				
			}
			
		}
		.commentBottomBar{
			width: 100%;
			padding-bottom: 200rpx;
			padding-top: 60rpx;
			display: flex;
			justify-content: center;
			color: rgb(153, 153, 153);
		}
	}
	.bottom{
		width: 100%;
		position: fixed;
		bottom: 0rpx;
	}
</style>
