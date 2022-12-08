<template>
	<view class="flex-col page-all">
		<view v-if="judgeRoute()" @click="goBack()" class="back">
			<u-icon name="play-left-fill"></u-icon>
		</view>
		<view class="flex-col items-center upper-half">
			<image @click="changeBGImg()"
				:src="bgimg || 'https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423182340154820481.png'"
				class="bg-image" />
			
      <!-- card -->
      <view class="flex-col section">
				<view class="justify-center nickname">
					<text>{{userInfo.openname}}</text>
				</view>
				<view class="justify-center follow">
					<view @click="goFollow()" class="group" style="text-align: right;">
						<text class="text_1">{{followNum}}</text>
						<text class="text_2">关注</text>
					</view>
					<view style="margin:0 38rpx;color: #e6e6e6ee;">|</view>
					<view @click="goFans()" class="group">
						<text class="text_1">{{fansNum}}</text>
						<text class="text_2">粉丝</text>
					</view>
				</view>
				<view class="justify-between btn-group">
					<template v-if="!isme">
						<view @click="follow()" :class="{'text-wrapper_1':followState==='关注'}" class="flex-col items-center text-wrapper" >
							<text>{{followState}}</text>
						</view>
						<view @click="chatWith()" class="flex-col items-center text-wrapper_1">
							<text>私信</text>
						</view>
					</template>

					<template v-else>
						<view @click="goEditInfo()" class="flex-col items-center text-wrapper_1">
							<text>编辑</text>
						</view>
						<view @click="goSetting()" class="flex-col items-center text-wrapper_1">
							<text>设置</text>
						</view>
					</template>

				</view>
			</view>

			<image @click="preview()" :src="userInfo.avatar" class="avatar" />
		</view>

		<view class="flex-col down-half">
						
      <!-- 资料完整度 -->
      <view v-if="isme && percentage!=100" class="flex-col justify-around card">
        <view class="flex-row justify-between top">
          <view class="top-left">资料完成度</view>
          <view class="flex-row" @click="goEditInfo()">
            <u-icon name="edit-pen-fill" color="#303030" size="22"></u-icon>
            <text class="top-right">去完善</text>
          </view>
        </view>
        <u-line-progress :percentage="percentage" activeColor="#f56c6c"/>
        <view class="bottom word666 flex-row">
          <u-icon name="file-text" size="20"></u-icon>
          <text style="font-size:27rpx">完成度达到60%，就可以发布动态啦!</text>
        </view>
      </view>

      <view id="all" class="flex-col section-1">
        <p class="title">个人名片</p>
        <text class="des"><text class="secTitle">简介：</text>{{userInfo.userResume ||  '这个人很懒什么也没留下'}}</text>
        <text class="des"><text class="secTitle">教育经历：</text>{{userInfo.userEdu ||  '保密'}}</text>
        <text class="des"><text class="secTitle">项目经验：</text>{{userInfo.userExpr ||  '保密'}}</text>
        <text class="des"><text class="secTitle">擅长技能：</text></text>
        <view v-if="skillList.length!=0" class="tag-wrap flex-row">
          <view v-for="(item,index) in skillList" :key="index" class="tag-item">
            <u-tag :text="item" plainFill type="error" plain></u-tag>
          </view>
        </view>
          <view v-else>
          这个人很懒什么也没留下
          </view>
        <text class="des"><text class="secTitle">获奖情况：</text></text>
          <view v-if="prizeList.length!=0" class="tag-wrap flex-row">
          <view v-for="(item,index) in prizeList" :key="index" class="tag-item">
            <u-tag :text="item" plainFill type="error" plain></u-tag>
          </view>
          </view>
          <view v-else>
          这个人很懒什么也没留下
          </view>
      </view>
      
      <view class="dynamic" @click.stop="toPost(userInfo.userId)" >
        <view class="container">
          <view class="left">{{isme?'我的动态':'Ta的动态'}}</view>
          <view v-if="isme" class="right" @click.stop="toCreate()"><u-icon name="edit-pen-fill" color="#303030" size="22"></u-icon>发布动态</view>
          <view v-else></view>
        </view>
        <view class="postCard" v-if="myPost.length!=0">
          <post-item fromPage="user" :postInfo='myPost[0]'/>
        </view>
      </view>
    
      <view id="collect" class="flex-col section-1" v-if="collectList.length">
        <p class="title">收藏</p>
        <u-empty v-if="!collectList.length" text='暂无' mode="data" icon="http://cdn.uviewui.com/uview/empty/data.png"/>
        <view v-for="(item,index) in collectList" :key="item.teamId" class="u-border-bottom line-item">
          <text @click="goDetail(item)" class="title-text">{{item.teamTitle}}</text>
          <view class="icon" v-if="isme" @click="removeCollect(item,index)">
            <u-icon name="trash-fill" color="#fff"></u-icon>
          </view>
        </view>
      </view>
    
      <view id="public" class="flex-col section-1" v-if="sendList.length">		
        <p class="title">发布</p>
        <u-empty v-if="!sendList.length" text='暂无' mode="data" icon="http://cdn.uviewui.com/uview/empty/data.png"/>
        <view v-for="(item,index) in sendList" class="u-border-bottom line-item" :key="item.teamId">
          <text @click="goDetail(item)" class="title-text">{{item.teamTitle}}</text>
          <view class="icon" v-if="isme" @click="removeSend(item,index)">
            <u-icon name="trash-fill" color="#fff"></u-icon>
          </view>
        </view>
      </view>
    
      <view id="join" class="flex-col section-1" v-if="joinList.length">
        <p class="title">参与</p>
        <u-empty v-if="!joinList.length" text='暂无' mode="data" icon="http://cdn.uviewui.com/uview/empty/data.png"/>
        <view v-for="(item,index) in joinList" :key="item.teamId" class="u-border-bottom line-item">
          <text @click="goDetail(item)" class="title-text">{{item.teamTitle}}</text>
        </view>
      </view>
					
		</view>
	</view>
</template>

<script>
	import segmenter from "@/components/common/segmenter.vue"
	import postItem from 'components/post/post-item'
	import {uploadFile,updateIntro,getFollowNum,getFansNum,followHim,unfollowHim,hasFollowed,getCollectTeam,getMyJoinTeam,getTeam,unCollectTeam,deleteTeam,getUserIntro,getUserById,getMyPost} from "@/common/API.js"
	export default {
		components: {
			segmenter,
			postItem
		},
		props: {
			userInfo: {
				type: Object,
				default: ()=>{return {}}
			}
		},
		data() {
			return {
				showLogin: false,
				isme: false,
				bgimg:this.userInfo.userBgimg,
				followState: '关注',
				followNum: '-',
				fansNum: '-',
				skillList:[],
				prizeList:[],
				collectList:[],
				sendList:[],
				joinList:[],
				page:0,
				allHeight:{},
				myPost:[],
        percentage:0,
			};
		},
		mounted() {
		  this.$disp.on('event.fresh_page_me',()=>{ this.initData() })
		  this.initData() 
		},
		methods: {
      initData(){
        // clear all data
        if(this.userInfo.userid == uni.getStorageSync('userId')) this.isme = true
        this.skillList = []
        this.prizeList = []
        this.myPost = []
        this.followNum = '-'
        this.fansNum = '-'
        this.collectList = []
        this.joinList = []
        this.percentage = 0

        // processing data
        this.userInfo.userSkill.split('^').forEach((item,index)=>{
          if(index!=0){ this.skillList.push(item) }
        })
        this.userInfo.userPrize.split('^').forEach((item,index)=>{
          if(index!=0){ this.prizeList.push(item) }
        })
        this.percentage = this.$common.calPercentage(false)[0]

        // get more data
        getMyPost(this.userInfo.userId,1,1).then(res=>{this.myPost=res},err=>{console.log(`[error] getMyPost:${err}`)})
        getFollowNum(this.userInfo.userid).then(res => { this.followNum = res })
        getFansNum(this.userInfo.userid).then( res => { this.fansNum = res })
        hasFollowed(uni.getStorageSync('userId'),this.userInfo.userid).then(bool => {if(bool) this.followState = '已关注'})
        getCollectTeam(this.userInfo.userid).then(rs => {this.collectList = rs})
        getTeam({teamLeader:this.userInfo.userid}).then(rs => {this.sendList = rs})
        getMyJoinTeam(this.userInfo.userid,this.page).then(rs => { this.joinList = rs})
      },
			judgeRoute() {
				let pages = getCurrentPages();
				let cur = pages[pages.length-1];
				return cur.route.includes('user')
			},		
			removeCollect(item,index) {
				uni.showModal({
					title:'提示',
					content:'确认取消收藏？',
					success: (rs) => {
						if(rs.confirm) {
							this.collectList.splice(index,1)
							unCollectTeam(this.userInfo.userid,item.teamId)
						}
					}
				})
			},
			removeSend(item,index) {
				uni.showModal({
          title: '提示',
          content: '你确定要删除该组队？',
          success: (res) => {
              if (res.confirm) {
            this.sendList.splice(index,1)
            deleteTeam(item.teamId)
              }
          }
				});
			},
			preview() {
				uni.previewImage({urls:[this.userInfo.avatar]})
			},
			changeBGImg() {
				if(!this.isme) return;
				uni.chooseImage({
					count:1,
					success: (e) => {
						var tempUrl = e.tempFilePaths[0]
						this.bgimg = tempUrl
						uploadFile(tempUrl).then(url => {
							updateIntro({userId:this.userInfo.userid,userBgimg:url})
							var u = uni.getStorageSync('userInfo');u.userBgimg = url; uni.setStorageSync('userInfo',u)
						})
					}
				})
			},
			async follow() {
				if(!uni.getStorageSync('hasLogin')){
				  uni.showToast({title:'请在登录后进行操作',icon:'none'})
				  return
				}
				if(this.followState=='关注') {
					await followHim(uni.getStorageSync('userId'),this.userInfo.userid)
					this.followState = '已关注'
					this.fansNum++
				} else {
					await unfollowHim(uni.getStorageSync('userId'),this.userInfo.userid)
					this.followState = '关注'
					this.fansNum--
				}
			},
			chatWith() {
				if(!uni.getStorageSync("hasLogin")) {uni.$u.toast('请在登录后进行操作');return}
				uni.navigateTo({ url: `/subPackageA/pages/message/chat?youId=${this.userInfo.userid}` })
				var msgList = uni.getStorageSync('msgList')
				msgList = new Map(msgList)
				var p = msgList.get(this.userInfo.userid)
				if(!p) { //列表无此人
					msgList.set(this.userInfo.userid,{
						avatar:this.userInfo.avatar,
						badge: 0,
						charContext:'',
						chatRecipientId:uni.getStorageSync('userId'),
						chatSenderId:this.userInfo.userid,
						chatTime:new Date().valueOf(),
						openname:this.userInfo.openname
					})
					uni.setStorageSync('msgList',[...msgList])
					console.log(msgList);
				}
				
			},
			goEditInfo() {
				uni.navigateTo({url:'/subPackageA/pages/me/me-edit'})
			},
			goSetting() {
				uni.navigateTo({url:'/subPackageA/pages/me/me-setting'})
			},
			goLogin() {
				uni.navigateTo({url:'/subPackageA/pages/me/login'})
			},
			goFollow() {
				uni.navigateTo({url:'/subPackageA/pages/me/me-follow?userId='+this.userInfo.userid})
			},
			goFans() {
				uni.navigateTo({url:'/subPackageA/pages/me/me-fans?userId='+this.userInfo.userid})
			},
			goBack() {
				uni.navigateBack()
			},
			goDetail(item) {
				uni.navigateTo({url:`/subPackageA/pages/home/teamDetail?teamId=${item.teamId}&teamLeader=${item.teamLeader}`})
			},
			toPost(e){
				if(this.isme){
					uni.navigateTo({
						url:'/subPackageA/pages/me/myPosts?userId='+uni.getStorageSync('userId')
					})
				}else{
					uni.navigateTo({
						url:'/subPackageA/pages/me/hisPosts?userId='+e
					})
				}
				
			},
			toCreate(){
        if(this.$common.calPercentage()[0] < 60) return
				uni.navigateTo({ url:'/subPackageA/pages/post/create' })
			}
		}
	};
</script>

<style scoped lang="scss">
	
	.page-all {
		background-color: #f5f5f5;
		width: 100%;
		height: 100%;
		overflow-y: auto;

		.back {
			 border-radius: 50%;
			 width: 56rpx;
			 height: 56rpx;
			 background-color: #D2E1E5;
			 color: #7d8487;
			 display: flex;
			 justify-content: center;
			 align-items: center;
			 position: fixed;
			 top: 97rpx;
			 left: 34rpx;
			 z-index: 99;
		}
		
		.upper-half {
			padding-bottom: 282rpx;
			position: relative;

			.bg-image {
				width: 100vw;
				height: 72vw;
			}

			.section {
				padding: 96rpx 100rpx 28rpx 128rpx;
				background-color: rgb(255, 255, 255);
				box-shadow: 2rpx 2rpx 8rpx 0px rgba(210, 210, 210, 0.5);
				border-radius: 18rpx;
				position: absolute;
				left: 52rpx;
				right: 52rpx;
				bottom: 0;

				.nickname {
					color: rgba(66, 64, 64, 0.78);
					font-size: 40rpx;
					font-weight: 600;
					line-height: 56rpx;
					white-space: nowrap;
					align-items: center;

					.image {
						width: 44rpx;
						height: 44rpx;
						position: absolute;
						right: 2%;
						top: 3%;
					}
				}

				.follow {
					margin-top: 22rpx;
					color: rgb(149, 149, 149);

					.group {
						white-space: nowrap;
						height: 42rpx;
						flex: 1;

						.text_1 {
							font-size: 30rpx;
							font-weight: 600;
							line-height: 42rpx;
						}

						.text_2 {
							margin-left: 10rpx;
							font-size: 26rpx;
							font-weight: 600;
							line-height: 36rpx;
						}
					}

					.level {
						font-weight: 600;
						line-height: 41rpx;
						white-space: nowrap;
					}
				}

				.btn-group {
					margin-top: 52rpx;
					padding: 0 14rpx;

					.text-wrapper {
						padding: 10rpx 0;
						color: rgb(149, 149, 149);
						font-size: 24rpx;
						font-weight: 1000;
						line-height: 34rpx;
						white-space: nowrap;
						background-color: rgb(243, 243, 243);
						border-radius: 29rpx;
						width: 162rpx;
						height: 58rpx;
						border: solid 2rpx #e4e4e4;
					}

					.text-wrapper_1 {
						// margin-right: 42rpx;
						padding: 8rpx 0 10rpx;
						color: rgb(65, 65, 65);
						font-size: 26rpx;
						font-weight: 600;
						line-height: 36rpx;
						white-space: nowrap;
						background-color: rgb(255, 255, 255);
						border-radius: 29rpx;
						width: 162rpx;
						height: 58rpx;
						border: solid 2rpx #e4e4e4;
					}
				}
			}

			.avatar {
				width: 142rpx;
				height: 142rpx;
				border-radius: 50%;
				position: absolute;
				bottom: 282rpx;
				left: 52%;
				transform: translateX(-50%);
        border:6rpx solid #fff;
			}
		}
		.seg {
			margin-top: 24rpx;
			white-space: initial;
		}
		.down-half {
			margin-top: 35rpx;
			padding: 0 40rpx 40rpx 40rpx;
			// height: calc(50vh - var(--window-top));
			width: 100%;
			.swiper-box {
				flex: 1;

			}
		
			.swiper-item {
				height: 100%;
			}
			.dynamic{
				padding: 30rpx 34rpx 30rpx 34rpx;
				border-radius: 18rpx;
				margin-bottom: 25rpx;
				background-color: #ffffff;
				display: flex;
				flex-direction: column;
				align-items: center;
				.container{
					width: 600rpx;
					height: 100rpx;
					display: flex;
					justify-content: space-between;
					align-items: center;
					
					.left{
						font-family: HiraginoSansGB-W6;
						font-size: 18px;
						color: #303030;
					}
					.right{
						font-family: HiraginoSansGB-W6;
						font-size: 12px;
						color: #303030;
						display: flex;
						align-items: center;
					}
					
				}
			}
      .card{
        background-color: rgb(255, 255, 255);
        width: 665rpx;
        padding: 43rpx 34rpx 50rpx 34rpx;
        border-radius: 18rpx;
        margin-bottom: 20rpx;
        margin: 0 auto 20rpx auto;

        .top{
          margin-bottom: 30rpx;
        }
        .top-left{
          font-family: HiraginoSansGB-W6;
					font-size: 18px;
					color: #303030;
        }
        .top-right{
          font-family: HiraginoSansGB-W6;
          font-size: 12px;
          color: #303030;
          display: flex;
          align-items: center;
        }
        .bottom{
          background-color: #ececec;
          border-radius: 10rpx;
          width: 600rpx;
          margin: 20rpx auto 0 auto;
          padding: 20rpx 20rpx 20rpx 20rpx;
        }
        
        
      }
			.section-1 {
				padding: 43rpx 34rpx 75rpx 34rpx;
				color: rgb(137, 137, 137);
				font-size: 28rpx;
				// font-weight: 600;
				line-height: 40rpx;
				white-space: nowrap;
				background-color: rgb(255, 255, 255);
				border-radius: 18rpx;
				height: auto;
				margin-bottom: 20rpx;

				.nofind {
					margin-left: 30rpx;
					margin-top: 20rpx;
				}

				.title {
					font-family: HiraginoSansGB-W6;
					font-size: 18px;
					color: #303030;
				}

        .line-progress{
          top: 120rpx;
          left: 100rpx;
          width: 100rpx;
        }

        .tag-wrap{
          flex-wrap: wrap;

          .tag-item{
            // padding: 20rpx;
            margin: 10rpx 10rpx 10rpx 10rpx ;
            // border:2rpx solid #000;
          }
        }

				.des {
					margin-top: 38rpx;
					white-space: normal;
					
					.secTitle {
						color: #505050;
					}
				}
				
				.right-btn {
					position: relative;
					left: 76%;
					margin-bottom: 20rpx;
				}
				
				.line-item {
					padding: 18rpx 20rpx;
					display: inline-flex;
					justify-content: space-between;
					
					.title-text {
						line-height: 60rpx;
					}
					
					.icon {
						width: 60rpx;
						height: 60rpx;
						border-radius: 50%;
						background-color: #f56c6c;
						margin: 0 6px;
						justify-content: center;
						align-items: center;
						display: flex;
					}
					
					
				}
				
			}
		}
	}
</style>