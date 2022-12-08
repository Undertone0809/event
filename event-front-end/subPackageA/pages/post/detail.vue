<template>
	<view v-if="!show">
		<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
			<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
				title loading avatar></u-skeleton>
		</view>
	</view>
	<view v-else class="page-all">
		<!-- 页面上部分信息 -->
		<view class="top" >
			<!-- 用户信息 -->
			<view class="user">
				<view class="userIntro" @click="goUser()">
					<image :src="post.avatar" mode="aspectFill" class="userHead"></image>
					<view class="userInfo">
						<view class="userName">{{post.openname}}</view>
						<view class="userEmail">{{new Date(post.postUptime).valueOf() | timeFrom }}</view>
					</view>
				</view>
				<view class="buttons">
          <!-- 关注 -->
          <view v-if="!isMyPost" @click="toAttention()" class="attention" :style="{'background-color': attentionColor}">
            {{attentionSet}}
          </view>
          <!-- 这个css代码对flex的优化不太行，浪费一行代码写一个空的占位符出来 -->
          <view v-else class="attention"></view>
          
          <!-- 更多 -->
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
			<!-- 内容信息 -->
			<view class="content">
				<view class="contentText">
					{{post.postContent}}
				</view>
				<view class="album" v-if="post.postImgUrl">
					<u-album :urls="post.postImgUrl?post.postImgUrl.split(' ') : []" singleSize="280" multipleSize="28vw" maxCount="9" space="2" ></u-album>
				</view>
			</view>
			<view class="share">
				<u-button text="分享给好友" open-type="share" shape="circle" :plain="true" icon="weixin-fill" iconColor="#09bb07"  ></u-button>
			</view>
      <view>
        <!-- !!!! -->





      </view>
		</view>

		<!-- 页面中间部分信息 -->
		<view class="middle" v-if="post.postId">
			<view class="title" v-if="comment.length!=0">
				<view v-if="commentNum!='-'" class="titleText">
					评论 {{commentNum}}
				</view>
			</view>
			<!-- 评论单位楼层 -->
			<view class="talk" v-for="(item,index) in comment" :key="item.commentId" >
				<!-- 评论用户信息 -->
				<view class="cuser" @click="isMyComment(item)">
					<view class="cuserIntro" @click="goUser(item)">
						<image :src="item.avatar" mode="aspectFill" class="cuserHead"></image>
						<view class="cuserInfo" @click="toReplyFirst(index)">
							<view class="cuserName">{{item.openname}}</view>
						</view>
					</view>
					<image class="more" src="../../static/post/more.png" @click="toShowMoreComment()">
				</view>
				<!-- 评论内容 -->
				<view class="ccontent" @click="toReplyFirst(index)">
					{{item.commentContent}}
				</view>

        <!-- 二级评论 -->
        <view v-if="item.reply" class="reply-wrap" @click="toReplyDetail(item)">
          <view class="flex-row reply-item-wrap" v-for="(replyItem,replyIndex) in item.reply" :key="replyIndex">
            <view class="reply-item-left word666" @click.stop="goUser(item)">{{replyItem.openname}}</view>
            <view class="reply-item-middle">
              <u-tag v-if="post.openname==replyItem.openname" text="作者" type="primary" size="mini" plain/>
            </view>
            <view class="reply-item-right">: {{replyItem.commentContent}}</view>
          </view>
        </view>

				<!-- 二级评论单位楼层（弃用） -->
				<view v-if="false">
					<view class="reply" v-for="(replyItem,replyIndex) in item.reply" :key="replyItem.commentId">
						<!-- 二级评论用户信息 -->
						<view class="cuser" @click="isMyComment(replyItem)">
							<view class="cuserIntro" @click="goUser(item)">
								<image :src="replyItem.avatar" mode="aspectFill" class="replyUserHead"></image>
								<view class="cuserInfo">
									<view class="replyUserName">{{replyItem.openname}}</view>
									<view class="replyUserName" v-if="replyItem.replyName"> <image src="../../static/post/to.png" style="width: 20rpx;height: 20rpx;margin: 0 10rpx;"></image> <text style="font-weight: bold;">{{replyItem.replyName}}</text></view>
								</view>
							</view>
							<image class="more" src="../../static/post/more.png" @click="toShowMoreComment()">
						</view>
						<!-- 二级评论内容 -->
						<view class="replyContent" @click="toReplyDetail(item)">
							{{replyItem.commentContent}}
						</view>
						<!-- 二级评论基本信息 -->
						<view class="commentInfo replyInfo" @click="toReplyDetail(item)">
							<!-- 二级评论时间 -->
							<view class="commentTime">
								{{new Date(replyItem.commentTime).valueOf() | timeFrom }}
							</view>
							<!-- 二级评论的点赞和回复 -->
							<view class="commentDetail">
								<image class="commentTwice" src="/static/post/comment.png"></image>
							</view>
						</view>
					</view>
					<view class="limit" v-if="replyIndex==1" @click="toReplyDetail(item)">
						详情><image src="/static/post/to.png" style="width: 20rpx;height: 20rpx;margin-left: 5rpx;">
					</view>
				</view>
				
        <!-- 评论基本信息 -->
				<view class="commentInfo">
					<!-- 评论时间 -->
					<view class="commentTime" @click="toReplyFirst(index)">
						{{new Date(item.commentTime).valueOf() | timeFrom }}
					</view>
					<!-- 评论的点赞和回复 -->
					<view class="commentDetail">
						<image class="commentTwice" src="/static/post/comment.png" @click="toReplyFirst(index)"></image>
						<!-- <image class="commentLike" src="/static/home/like.png" @click="like(index)" v-if="item.isLike==false"></image>
						<image class="commentLike" src="/static/home/like-fill.png" @click="like(index)" v-else></image>
						<text @click="toReplyFirst(index)">{{item.commentLikes}}</text> -->
					</view>
				</view>
      
      </view>
			<view class="commentBottomBar">
				---没有更多评论了---
			</view>
		</view>
		<!-- 底部输入栏(从chat.vue粘贴修改的...) -->
		<sendInput ref="sendInput" v-on:send_detail="send()" :holdWord="holdWord" inPage="detail"></sendInput>
	</view>
</template>
<script>
import {getPost,getTopComment,getSubLE2,sendComment,hasFollowed,followHim,unfollowHim,removePost,removeComment,getComment} from '@/common/API.js'
import sendInput from "../../components/common/sendInput.vue"
export default {
	components: { sendInput },
	data() {
		return {
			show:false,
			showMorePost:false,//展示帖子的更多选项变量
			showMoreComment:false,//展示评论的更多选项变量
			isMyPost:false,//是否是自己的帖子
			attentionSet:"＋关注",//关注按钮的内部文字
			moreSet:"···",//更多按钮的内部文字
			isAttention:false,//判断是否关注的状态变量
			attentionColor:'#dd524d',//关注时的颜色
			replyType:"comment",//评论种类(共三种):1.comment直接在评论区建立一个评论 2.first 在评论区中的评论里建立一个二级评论 3.second 回复一个二级评论 
			index:0,//用来获取一级评论位置的变量
			replyIndex:0,//用来获取二级评论位置的变量
			holdWord:"发表评论...",//textarea中的文字,回复他人时会改变
			replyName:"",//在二级评论中回复的对象的用户名
			post:{},
			comment:[],
			RemoveList:
      [{ name:'删除',color:'#dd524d' },
      { name: '分享' }],
			NotRemoveList: 
      [{ name:'举报',color:'#dd524d' },
      { name: '分享' }],
			postId:'',
			canRemovePost:false,
			canRemoveComment:false,
			onSelectCommentId:'',
			onSelectType:"",
			commentNum:"-"
		}
	},
  // 分享给好友
  onShareAppMessage() {
    return {
      title: '快来看看他的想法',
      path: '/subPackageA/pages/post/detail?postId='+this.postId,
    }
  },
	onShow() {
		uni.$on("freshComment",res => {
			if(res){
				this.comment.forEach(function(item, index, arr) {
				if(item.commentId == res) arr.splice(index, 1)
					if(item.reply!=null){
						item.reply.forEach(function(replyItem,replyIndex,replyArr){
							if(replyItem.commentId == res) replyArr.splice(replyIndex, 1)
						})
					}
				})
			}else{
				var p0 = getPost({postId:this.postId})
				var p1 = getTopComment(this.postId)
				Promise.all([p0,p1]).then( async rs => {
					if(uni.getStorageSync('userId')) hasFollowed(uni.getStorageSync('userId'),rs[0][0].userId).then(r => this.isAttention = r)
					this.post = rs[0][0];
					var top = rs[1];
					if(!top) this.show = true;
					else {
						for(let item of top) await getSubLE2(item.commentId).then(r => item.reply = r?r.slice(0,2):r)
						this.comment = top
						this.show = true
					}
				}).then(()=>{  })
			}
			
		})
		
	},
	onLoad(option) {
		this.postId=option.postId;
		if(option.commentNum) this.commentNum=option.commentNum;
		var p0 = getPost({postId:option.postId})
		var p1 = getTopComment(option.postId)
		Promise.all([p0,p1]).then( async rs => {
      this.post = rs[0][0]
      // 判断是否为自己或是否登陆，如果是，则不渲染button
      if(!uni.getStorageSync('userId') || uni.getStorageSync('userId') == this.post.userId){
        this.isMyPost = true
        this.canRemovePost=true
      }else{
        // 是否关注了对方，res返回true则表明已关注
        this.canRemoveComment=false;
        hasFollowed(uni.getStorageSync('userId'),this.post.userId).then(res =>{
        if(res){
          this.attentionSet="已关注";
          this.isAttention=true;
          this.attentionColor="#999";
        }
		  })
      } 
      
			var top = rs[1];
			if(!top) this.show = true;
			else {
				for(let item of top) {
					await getSubLE2(item.commentId).then(r => item.reply = r?r.slice(0,2):r )
				}
				this.comment = top
				this.show = true
			}
		})
		console.log(this.post)
	},
	methods: {
		// 用户关注
		toAttention(){
			if(!uni.getStorageSync('hasLogin')){
			  uni.showToast({title:'暂未登录',icon:'none'})
			  return
			}
			let myid = uni.getStorageSync('userId')
			if(!myid) return
			if(this.isAttention==false)
			{
				followHim(myid,this.post.userId)
				this.attentionSet="已关注";
				uni.showToast({
					title:'关注成功',
					duration:2000
				});
				this.isAttention=true;
				this.attentionColor="#999";
			}
			else{
				unfollowHim(myid,this.post.userId)
				this.attentionSet="+关注";
				uni.showToast({
					title:'取消关注成功',
					duration:2000
				});
				this.isAttention=false;
				this.attentionColor="#dd524d";
			}
		},
		// 点赞一级评论方法
		like(index){
			if(this.comment[index].isLike==false)
			{
				this.comment[index].isLike=true
				this.comment[index].like=this.comment[index].like+1;
			}
			else
			{
				this.comment[index].isLike=false
				this.comment[index].like--;
			}
			
		},
		// 在评论区发表一级评论方法
		send(textMsg){
      // 判断是否完善个人信息
      if(this.$common.calPercentage()[0] < 60) return 
      
      uni.showLoading({title:'请稍后'})
      console.log(`[info] input: ${textMsg}`);
      let me = uni.getStorageSync('userInfo')
      let it = {
        commentId:"comment"+this.comment.length,
        avatar:me.avatar,
        openname:me.openname,
        commentContent:textMsg,
        commendLikes:0,
        commentTime:new Date(),
        reply:[],
        userId:me.userId
      }
      // 一级评论
      if(this.replyType == 'comment') {
        let t = {commentContent:it.commentContent, postId:this.post.postId, userId:me.userId}
        this.$refs.sendInput.textMsg="";
        sendComment(t).then(() => {
          // 刷新页面
          if(!this.comment) this.comment = []
          var p0 = getPost({postId:this.postId})
          var p1 = getTopComment(this.postId)
          Promise.all([p0,p1]).then( async rs => {
            if(uni.getStorageSync('userId')) hasFollowed(uni.getStorageSync('userId'),rs[0][0].userId).then(r => this.isAttention = r)
            this.post = rs[0][0];
            var top = rs[1];
            if(!top) this.show = true;
            else {
              // 查询对应的二级评论，查两条显示在UI上
              for(let item of top) {
                await getSubLE2(item.commentId).then(r => item.reply = r?r.slice(0,2):r )
              }
              this.comment = top
              this.show = true
              // commentNum +1  
              this.commentNum = parseInt(this.commentNum)+1
            }
          }).then(()=>{ uni.$u.toast('评论成功') })
          uni.hideLoading()
        }).catch(()=>{
          uni.showToast({ title: '评论失败,请联系开发者',icon: 'none',duration: 1000 })
          uni.$emit("freshComment",null)
        })
      
      // 二级评论
      } else {
        let t = {commentContent:it.commentContent, postId:this.post.postId, userId:me.userId,faCommentId:this.comment[this.index].commentId}
        sendComment(t).then(() => {
          var p0 = getPost({postId:this.postId})
          var p1 = getTopComment(this.postId)
          Promise.all([p0,p1]).then( async rs => {
            if(uni.getStorageSync('userId')) hasFollowed(uni.getStorageSync('userId'),rs[0][0].userId).then(r => this.isAttention = r)
            this.post = rs[0][0];
            var top = rs[1];
            if(!top) this.show = true;
            else {
              for(let item of top) {
                await getSubLE2(item.commentId).then(r => item.reply = r?r.slice(0,2):r )
              }
              this.comment = top
              this.show = true
            }
          }).then(()=>{
            let t = this.comment[this.index].reply
            if(t==null) {
              this.comment[this.index].reply = [];
              t=[];
            }
            //if(t.length==2) this.comment[this.index].reply.pop()
            // commentNum +1  
            this.commentNum = parseInt(this.commentNum)+1
            uni.$u.toast('回复成功')
          })
          this.holdWord="发表评论...";
          this.index=0;
          this.replyType="comment";
          uni.hideLoading()
        }).catch(()=>{
          uni.showToast({ title: '评论失败',icon: 'none',duration: 1000 })
          uni.$emit("freshComment",null)
        })
        
      }
		},
		// 点击一级评论弹出软键盘且显示回复对象方法
		toReplyFirst(index){
			this.$refs.sendInput.isFocus=true;
			this.replyType="first";
			this.index=index;
			this.holdWord="回复 "+this.comment[index].openname;
		},
		toReplyDetail(e){
			let {commentId} = e
			uni.navigateTo({url:'replyDetail?commentId='+commentId})
		},
		goUser(item) {
			if(item) uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+item.userId})
			else uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+this.post.userId})
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
			if(index.name=='分享') getCurrentPages().pop().onShareAppMessage()
      
      if(index.name=="删除"&&this.canRemovePost==true&&this.onSelectType=="Post"){
				uni.showModal({
					title:'提示',
					content:'确认删除该想法吗？',
					success: (res) => {
						if (res.confirm){
							removePost(this.postId);
							this.$store.commit("setFresh", true)
							uni.navigateBack({ delta:1 })
              setTimeout(() => {
                this.$disp.fire(`event.fresh_myPosts`)  
              }, 500);
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
							console.log(this.comment) 
							var that=this;
							this.comment.forEach(function(item, index, arr) {
                if(item.commentId == that.onSelectCommentId) arr.splice(index, 1)
								if(item.reply!=null)
									item.reply.forEach(function(replyItem,replyIndex,replyArr){
										if(replyItem.commentId == that.onSelectCommentId) replyArr.splice(replyIndex, 1)
									});
								
							})
							uni.$u.toast("删除评论成功")
						}
					}
				})
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
				
		},
		// toAll(){
		// 	console.log(4)
		// 	wx.showShareMenu({
		// 	  withShareTicket: true,
		// 	  menus: ['shareAppMessage']
		// 	})
		// }
	}
	
}
</script>

<style lang="scss" scoped>
	.top{
		width: 100%;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		box-shadow: 0px 2rpx 8rpx 0px rgba(165, 165, 165, 0.5);
		.content{
			width: 93%;
			font-size: 32rpx;
			padding-top: 33rpx;
			padding-bottom: 33rpx;
			text-align: justify;
			.contentText{
				padding-bottom: 24rpx;
			}
		}
		.share{
			width: 220rpx;
			height: 100rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding-bottom: 30rpx;
			.buttonIcon{
				width: 60rpx;
				height: 60rpx;
				background-color: blue;
			}
		}
	}
	.user{
		width: 93%;
		display: flex;
		justify-content: space-between;
		align-items: center;
		font-size: 28rpx;
		padding-top: 20rpx;
		padding-bottom: 10rpx;
		
		.userIntro{
			display: flex;
			align-items: center;
		}
		.userHead{
			width: 80rpx;
			height: 80rpx;
			border-radius: 40rpx;
			background-size: contain;
			margin-right: 30rpx;
		}
		.userName{
			font-weight: bold;
		}
		.userEmail{
			color: rgb(153, 153, 153);
			font-size: 26rpx;
		}
		.buttons{
			width: 180rpx;
			height: 50rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
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
			.more{
				width: 40rpx;
				height: 40rpx;
				
			}
		}
		
	}
	
	.middle{
		
		width: 100%;
		padding-top: 30rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		.title{
			width: 93%;
			height: 60rpx;
			display: flex;
			align-items: center;
			font-size: 36rpx;
			font-weight: bold;
			padding-bottom: 40rpx;
		}
		.talk{
			width: 93%;
			
			.cuser{
				width: 100%;
				height: 60rpx;
				
				display: flex;
				align-items: center;
				justify-content: space-between;
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
					margin-right: 32rpx;
					width: 40rpx;
					height: 40rpx;
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
        padding: 20rpx 20rpx 30rpx 90rpx;
				color: rgb(153, 153, 153);
				font-size: 26rpx;
				
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
      .reply-wrap{
        background-color: #f5f5f5;
				padding: 20rpx 20rpx 0 20rpx;
				margin-left: 90rpx;
				border-radius: 6rpx;
        overflow: hidden;
        

        .reply-item-wrap{
          margin: 0 0 20rpx 0;
          
          .reply-item-left{
            font-weight: 700;
            font-size: 28rpx;
          }
          .reply-item-middle{
            margin-left: 10rpx;
            margin-right: 10rpx;
          }
          .reply-item-right{
            font-size: 28rpx;
            width: 420rpx;
            word-break: break-all;
            text-overflow: ellipsis;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2; /* 这里是超出几行省略 */

          }
        }
      }
			.reply{
				background-color: #f5f5f5;
				padding: 20rpx 0 0 20rpx;
				margin-left: 90rpx;
				border-radius: 6rpx;
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
					overflow:hidden; 
					white-space:nowrap; 
					text-overflow:ellipsis;
				}
				.replyInfo{
					
					padding-left: 65rpx !important;
					
				}
			}
			.limit{
				width: 280rpx;
				padding: 10rpx 10rpx;
				background-color: rgb(241, 241, 241);
				margin-left: 90rpx;
				border-radius: 20rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				font-size: 26rpx;
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
