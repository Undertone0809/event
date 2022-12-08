<template>
    <!-- 组队详情页面 -->
	<view class="flex-col" v-if="show">
		<!-- #ifdef MP-QQ -->
		<modal-qq :show="showModal" title='申请码' v-model="codeValue" placeholder='4位数的申请码(向发起人索取)' @confirm="confirmCode()" @cancle="showModal=false,codeValue=''"></modal-qq>
		<!-- #endif -->
		<view class="flex-col post">
			<view class="top justify-between">
				<view @click="goUser()" class="flex-row">
					<image :src="item.avatar" class="avatar" />
					<view class="flex-col info">
						<text class="user-intro">{{item.email || '@none'}}</text>
						<text class="user-name">{{item.openname}}</text>
					</view>
				</view>
				<text class="send-time">{{ new Date(item.teamSendTime).valueOf() | timeFrom }}</text>
			</view>
			<view class="flex-col post-content" @click="toTip()">
				<view class="top flex-col">
					<text user-select=true class="title">{{item.teamTitle}}</text>
					<text user-select=true class="detail">{{item.teamDetail}}</text>
				</view>
				<view class="top ppt">
					<view>
						<view class="dot1"></view>
						<text user-select=true class="require">时间：{{formatTime(item.teamStartTime)}} - {{formatTime(item.teamEndTime)}} </text>
						<text user-select=true class="remain" :style="{color:remainColor}">{{remainTime()}}</text>
					</view>
					<view class="tw">
						<view class="dot2"></view>
						<text user-select=true class="require">人数：{{item.teamGetPnum}}/{{item.teamPnum}}人</text>
					</view>
					<view class="tw" v-if="item.teamPlace">
						<view class="dot3"></view>
						<text user-select=true class="require">地点：{{item.teamPlace}}</text>
					</view>
					<view class="tw" v-if="isme">
						<view class="dot4"></view>
						<text user-select=true class="require">申请码：{{item.teamPsw}}</text>
					</view>
				</view>
			</view>
			<view class="album" v-if="item.teamImgUrl">
				<u-album :urls="imgs" singleSize="280" multipleSize="28vw" maxCount="9" space="2" ></u-album>
			</view>
			<view class="join-user-list">
				<text v-for="(item,index) in joinList" @click="goUser(item)" :key="item.userid" class="join-list" :style="{backgroundImage:`url(${item.avatar})`}"></text>
				<text v-if="joinList.length" class="text">已加入</text>
			</view>
			<view v-if="isme&&joinList.length>0" class="join-user-detail">
				<view class="title">
					成员信息
				</view>
					<!-- <image :src="item.avatar" class="avatar" /> -->
					<view class="userInfo">
						<!-- <view class="infoLine">
							<view style="width: 100rpx;">昵称：</view><view>Seaaa</view>
						</view>
						<view class="infoLine">
							<view style="width: 100rpx;">邮箱：</view><view>319677245@qq.com</view>
						</view>
						<view class="infoLine">
							<view style="width: 100rpx;">QQ：</view><view>319677245</view>
						</view> -->
						<div class="friend-item" v-for="(item,index) in joinList">
							<userItem :item="item"></userItem>
						</div>
					</view> 
			</view>
			<view class="bottom">
				<view @click="collectIt()" style="margin-left: 30rpx;">
					<u-icon v-if="!option.hasCollect" label-size="16" name="star" size="24" label="收藏"></u-icon>
					<u-icon v-else label-size="16" name="star-fill" size="24" label="收藏"></u-icon>
				</view>
				<view @click="likeIt()" style="margin: 0 90rpx">
					<u-icon v-if="!option.hasLike" label-size="16" name="thumb-up" size="26" :label="option.likeNum"></u-icon>
					<u-icon v-else label-size="16" name="thumb-up-fill" size="26" :label="option.likeNum"></u-icon>
				</view>
				<view v-if="isme" class="btn">
					<view @click="deleteIt()" class="btn1">
						<text>删除</text>
					</view>
					<view @click="editIt()" class="btn2">
						<text>编辑</text>
					</view>
				</view>
				<view v-else class="btn">
					<view @click="chatWith()" class="btn1">
						<text>私聊</text>
					</view>
					<view @click="joinIt()" class="btn2">
						<text>{{joinText}}</text>
					</view>
				</view>
			</view>
			<u-popup :show="showTip" mode="center"  @close="close" @open="toTip" :round="15" :safeAreaInsetBottom="false">
				<view class="tip">
					<view class="tipImg"></view>
					<view class="tipWord">
						请与发布者联系进一步交流哦~
					</view>
				</view>
			</u-popup>
			<u-popup :show="showInfo" mode="center"  @close="close" @open="toTip" :round="15" :safeAreaInsetBottom="false">
				<view class="infos">
					<view class="infoTitle">
						发布者预留的联系方式为
					</view>
					<view class="infoContent">
						<view class="infoImg">
							
						</view>
						<view class="infoDetail">
							<text :selectable="true">
								{{item.teamContact}}
							</text>
						</view>
					</view>
					<view class="infoWord">
						祝合作愉快~
					</view>
				</view>
			</u-popup>
		</view>
	</view>
	<view style="margin-top: 30rpx;" v-else>
		<u-skeleton :rows="18" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%']" title loading avatar></u-skeleton>
	</view>
</template>

<script>
	import {deleteTeam,quitTeam,getTeam,joinTeam,hasJoinTeam,hasLikeTeam,hasCollectTeam,getLikeNum,likeTeam,disLikeTeam,collectTeam,unCollectTeam,getJoinUser,getUserById} from '@/common/API.js'
	import modalQq from "../../components/common/modal-qq.vue"
	import userItem from "@/components/contact/user-item.vue"
	export default {
		components:{modalQq,userItem},
		data() {
			return {
				show:false,
				showTip:false,
				showInfo:false,
				item:{},
				option:{},
				isme:false,
				joinText:'立即加入',
				joinList:[],
				remainColor:'#2CC900',
				// #ifdef MP-QQ
				showModal: false,
				codeValue: '',
				// #endif
			};
		},
		computed:{
			imgs() {
				return this.item.teamImgUrl ? this.item.teamImgUrl.split(' ') : []
			}
		},
		onPullDownRefresh() {
			getTeam({teamId:this.option.teamId}).then(rs => {this.item = rs[0]; uni.stopPullDownRefresh()})
		},
		async onLoad(option) {
			if(!uni.getStorageSync("hasLogin")) { //未登录需要特殊处理
				this.option = option
				option.hasLike = false;
				option.hasCollect = false
				let p0 = getTeam({teamId:option.teamId})
				let p1 = getLikeNum(option.teamId)
				let p2 = getJoinUser({teamId:option.teamId})
				await Promise.all([p0,p1,p2]).then(rs => {
					this.item = rs[0][0];
					option.likeNum = rs[1]
					this.joinList = rs[2]
					this.show = true;
				})
				return
			}
			
			getTeam({teamId:option.teamId}).then(rs => {
				console.log(rs)
				if(rs.length==1) this.item = rs[0]; else {uni.navigateBack(); uni.$u.toast('该组队已被删除！'); return}
				if(option.hasLike != undefined) this.show = true;})
			getJoinUser({teamId:option.teamId}).then(rs => {
				this.joinList=rs;	
			})
			hasJoinTeam(uni.getStorageSync('userId'),option.teamId).then(rs => {if(rs) this.joinText = '已加入'})
			this.option = option
			if(option.teamLeader == uni.getStorageSync('userId')) this.isme = true
			if(option.hasLike != undefined) {
				option.hasLike = option.hasLike=='true'; 
				option.hasCollect = option.hasCollect=='true'
			} else { //如果上一页没传现成的数据，就需要请求
				let p0 = getLikeNum(option.teamId)
				let p1 = hasLikeTeam(uni.getStorageSync('userId'),option.teamId)
				let p2 = hasCollectTeam(uni.getStorageSync('userId'),option.teamId)
				Promise.all([p0,p1,p2]).then(rs => {
					this.option.likeNum = rs[0]
					this.option.hasLike = rs[1]
					this.option.hasCollect = rs[2]
					this.show = true;
				})
			}
			// console.log('this.option',this.option);
		},
		methods: {
			updateBefore() {
				let pages = getCurrentPages();
				let before = pages[pages.length-2];
				before.onPullDownRefresh();
			},
			async likeIt() {
				if(this.option.hasLike) {
					disLikeTeam(uni.getStorageSync('userId'),this.item.teamId)
					this.option.hasLike = false
					this.option.likeNum--
				} else {
					await likeTeam(uni.getStorageSync('userId'),this.item.teamId)
					this.option.hasLike = true
					this.option.likeNum++
				}
				this.$forceUpdate()
				this.updateBefore()
			},
			async collectIt() {
				if(this.option.hasCollect) {
					uni.showModal({
						title:'提示',
						content:'确认取消收藏吗？',
						success: (res) => {
							if(res.confirm) {
								unCollectTeam(uni.getStorageSync('userId'),this.item.teamId)
								this.option.hasCollect = false
								this.$forceUpdate()
								this.updateBefore()
							} }
					})
				} else {
					await collectTeam(uni.getStorageSync('userId'),this.item.teamId)
					this.option.hasCollect = true
					this.$forceUpdate()
					this.updateBefore()
				}
			},
			// #ifdef MP-QQ
			async confirmCode() {
				if(this.codeValue==this.item.teamPsw) {
					this.showModal = false
					uni.showLoading({title:'请稍后..'})
					await joinTeam(uni.getStorageSync('userId'),this.item.teamId)
					await getJoinUser({teamId:this.option.teamId}).then(rs => {this.joinList = rs})
					this.$u.toast('加入成功！')
					this.joinText = '已加入'
					this.item.teamGetPnum++
				} else this.$u.toast('x申请码错误x')
			},
			// #endif
			joinIt() {
				if(!uni.getStorageSync("hasLogin")) {uni.$u.toast('请在登录后进行操作');return}
				if(this.joinText == '已加入') {
					uni.showModal({
						title:'提示',
						content:'你确定要退出该组队吗？',
						success:async (e) => { 
							if(e.confirm) {
								uni.showLoading({title:'请稍后..'})
								await quitTeam(uni.getStorageSync('userId'),this.item.teamId)
								await getJoinUser({teamId:this.option.teamId}).then(rs => {this.joinList = rs})
								uni.hideLoading();uni.$u.toast('已退出！')
								this.joinText = '立即加入'
								this.item.teamGetPnum--
							}
						}
					})
				}
				else {
					// #ifdef MP-QQ
					this.showModal = true;
					// #endif
					
					// #ifdef MP-WEIXIN
					uni.showModal({
					title:'申请码',
					editable:true, 
					placeholderText:'4位数的申请码(向发起人索取)',
					success: async (e) => { 
							if(e.confirm) 
								if(e.content==this.item.teamPsw) {
									uni.showLoading({title:'请稍后..'})
									await joinTeam(uni.getStorageSync('userId'),this.item.teamId)
									await getJoinUser({teamId:this.option.teamId}).then(rs => {this.joinList = rs})
									this.$u.toast('加入成功！')
									this.joinText = '已加入'
									this.item.teamGetPnum++
								} else this.$u.toast('x申请码错误x')
						}
					})
					// #endif
				}
				
			},
			chatWith() {
				if(!uni.getStorageSync("hasLogin")) {uni.$u.toast('请在登录后进行操作');return}
				uni.navigateTo({ url: `/subPackageA/pages/message/chat?youId=${this.item.teamLeader}` })
				var msgList = uni.getStorageSync('msgList')
				msgList = new Map(msgList)
				var p = msgList.get(this.item.teamLeader)
				if(!p) { //列表无此人
					msgList.set(this.item.teamLeader,{
						avatar:this.item.avatar,
						badge: 0,
						charContext:'',
						chatRecipientId:uni.getStorageSync('userId'),
						chatSenderId:this.item.teamLeader,
						chatTime:new Date().valueOf(),
						openname:this.item.openname
					})
					uni.setStorageSync('msgList',[...msgList])
				}
			},
			deleteIt() {
				uni.showModal({
					title:'警告',
					content:'确认要删除吗？',
					success: async (rs) => {
						if(rs.confirm) {
							await deleteTeam(this.item.teamId)
							uni.$u.toast('删除成功')
							this.updateBefore()
							uni.navigateBack()
						}
					}
				})
			},
			editIt() {
				uni.navigateTo({ url: '/subPackageA/pages/team/teamEdit?teamId='+this.item.teamId})
			},
			goUser(item) {
				if(item) uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+item.userid})
				else uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+this.item.teamLeader})
			},
			formatTime(time) {
				var t = new Date(time)
				var rs = ''
				if(t.getFullYear() == new Date().getFullYear()) {
					if(t.getMonth() == new Date().getMonth() && t.getDate() == new Date().getDate())
						rs = uni.$u.timeFormat(t,'hh:MM')
					else {
						if(t.getHours()==0 && t.getMinutes()==0)
							rs = uni.$u.timeFormat(t, 'mm-dd')
						else rs = uni.$u.timeFormat(t, 'mm-dd hh:MM')
					}
				}
				else rs = uni.$u.timeFormat(t, 'yyyy-mm-dd')
				return rs
			},
			remainTime() {
				var a1 = Date.parse(new Date());
				var a2 = Date.parse(new Date(this.item.teamEndTime));
				var diff = a2-a1
				if(diff<0) {this.remainColor = '#F08080';return ''}
				//计算出相差天数  
				var days=Math.floor(diff/(24*3600*1000))  
				//计算出小时数  
				var leave1=diff%(24*3600*1000)
				var hours=Math.floor(leave1/(3600*1000))  
				//计算相差分钟数  
				var leave2=leave1%(3600*1000)
				var minutes=Math.floor(leave2/(60*1000))  
				if(days == 0) return '剩余'+hours+"小时"+minutes+"分钟"
				return '剩余'+days+"天"+hours+"小时"
			},
			toTip(){
				if(!this.isme){
					if(this.joinText=="立即加入"){
						this.showTip = true
					}else{
						this.showInfo=true
					}
				}
				
			},
			close() {
				this.showTip = false
				this.showInfo=false
				// console.log('close');
			}
		}
	};
</script>

<style scoped lang="scss">

	.post {
		margin: 0 15rpx;
		padding: 28rpx 0 2rpx 4rpx;
		background-color: rgb(255, 255, 255);
		border-radius: 18rpx;

		.top {
			padding: 0 18rpx;

			.send-time {
				margin-right: 12rpx;
				align-self: center;
				color: rgb(153, 153, 153);
				font-size: 26rpx;
				line-height: 36rpx;
				white-space: nowrap;
			}

			.avatar {
				width: 72rpx;
				height: 72rpx;
				border-radius: 50%;
			}

			.info {
				margin-left: 30rpx;
				height: 72rpx;

				.user-intro {
					margin-top: 36rpx;
					color: rgb(153, 153, 153);
					font-size: 26rpx;
					line-height: 36rpx;
					white-space: nowrap;
				}

				.user-name {
					margin-top: -72rpx;
					color: rgb(0, 0, 0);
					font-size: 30rpx;
					line-height: 42rpx;
					white-space: nowrap;
				}
			}

			.title {
				color: rgb(45, 45, 45);
				font-size: 34rpx;
				font-weight: 500;
				line-height: 48rpx;
				white-space: nowrap;
			}

			.detail {
				margin-right: 18rpx;
				margin-top: 18rpx;
				color: #616161;
				font-size: 28rpx;
				line-height: 46rpx;
				text-align: left;
				overflow: hidden;
				letter-spacing: 0.7px;
			}
		}

		.post-content {
			margin-top: 40rpx;

			.ppt {
				margin-top: 34rpx;

				.tw {
					margin-top: 10rpx;
				}

				.dot1 {
					margin-right: 14rpx;
					margin-top: 18rpx;
					width: 16rpx;
					border-radius: 50%;
					height: 16rpx;
					background-color: rgb(255, 113, 113);
					display: inline-block;
				}

				.dot2 {
					margin-right: 14rpx;
					margin-top: 18rpx;
					width: 16rpx;
					border-radius: 50%;
					height: 16rpx;
					background-color: rgb(222, 127, 255);
					display: inline-block;
				}

				.dot3 {
					margin-right: 14rpx;
					margin-top: 18rpx;
					width: 16rpx;
					border-radius: 50%;
					height: 16rpx;
					background-color: rgb(83, 195, 255);
					display: inline-block;
				}
				
				.dot4 {
					margin-right: 14rpx;
					margin-top: 18rpx;
					width: 16rpx;
					border-radius: 50%;
					height: 16rpx;
					background-color: #ffde7a;
					display: inline-block;
				}

				.remain {
					color: rgb(44, 201, 0);
					font-size: 26rpx;
					font-weight: 500;
					line-height: 48rpx;
					margin-left: 10rpx;
				}

				.require {
					color: rgb(83, 83, 83);
					font-size: 28rpx;
					line-height: 48rpx;
				}

			}
		}

		.album {
			margin: 20rpx 18rpx 55rpx;
		}
		
		.join-user-list {
			margin-top: 60rpx;
			margin-bottom: 30rpx;
			.join-list {
				box-shadow: 0 0 0 2px rgb(248, 248, 248);
				margin-right: -20rpx !important;
				width: 88rpx;
				height: 88rpx;
				display: inline-block;
				background: #2ddcd3 no-repeat center/cover;
				vertical-align: bottom;
				border-radius: 50%;
				background: url('https://s2.loli.net/2022/02/09/pWvLIFPsZ9145gu.jpg') center center;
				background-repeat:no-repeat;
				background-size: cover;
				background-position:0px 0px; 
			}
			
			.text {
				margin-left: 26rpx;
				color: #676767;
				font-weight: 600;
				line-height: 88rpx;
				font-family: serif;
				font-size: 26rpx;
			}
		}
		.join-user-detail{
			border-top: solid 2rpx rgba(0, 0, 0, 0.1);
			padding-bottom: 150rpx;
			.title{
				width: 100%;
				height: 120rpx;
				font-size: 34rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				padding:0 30rpx;
				color: #636363;
				font-weight: bold;
			}
			.userInfo{
				display: flex;
				flex-direction: column;
				align-items: center;
				.friend-item {
					display: inline-block;
					width: 650rpx;
					/*height: 100px;*/
					//margin: 0 5% 20px 0;
					display: flex;
					justify-content: center;
					padding: 10px 30px;
					border: 1px solid #ECECEC;
					border-radius: 3px;
					margin-bottom: 30rpx;
					&:hover {
						box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
					}
				
					&:nth-of-type(3n) {
						margin-right: 0;
					}
				    .container{
						display: flex;
						justify-content: space-between;
						align-items: center;
						width: 100%;
						.left {
							display: inline-block;
							.avatar {
								width: 100rpx;
								height: 100rpx;
								margin-right: 40rpx;
								border-radius: 50%;
							}
						}
									
						.right {
							display: inline-block;
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
							.site-name,
							.site-name {
								border-bottom: 1px dotted #ECECEC;
								background-image: linear-gradient(-3deg, #ffffff 0%, #ff6464 100%);
								font-size: 41rpx;
								-webkit-background-clip: text;
								color: #636363;
								font-weight: 100;
								//font-family: monospace;
							}
									
							
						}
					}
					
				
				}
			}
			
			// .userItem{
			// 	width: 100%;
			// 	display: flex;
			// 	justify-content: space-between;
			// 	align-items: center;
			// 	.avatar{
			// 		width: 88rpx;
			// 		height: 88rpx;
			// 		border-radius: 50%;
			// 	}
			// 	.userInfo{
			// 		width: 600rpx;
			// 		display: flex;
			// 		flex-direction: column;
			// 		justify-content: space-between;
			// 		color: #676767;
			// 		font-size: 26rpx;
			// 		.infoLine{
			// 			width: 200rpx;
			// 			display: flex;
			// 			align-items: center;
			// 		}
			// 	}
				
			// }
		}

		
		.bottom {
			width: 100%;
			height: 155rpx;
			margin-top: 23rpx;
			border-top: #e9ecec solid 1px;
			display: flex;
			align-items: center;
			position: fixed;
			bottom: 0;
			background-image: linear-gradient(360deg, #ffffff 0%, #ffffff 85%);
			padding-bottom: 30rpx;
			padding-right: 51rpx;
			
			.btn {
				width: 100%;
				height: 100%;
				display: flex;
				align-items: center;
				
				.btn1 {
					width: 150rpx;
					height: 64%;
					border-radius: 34rpx 0 0 34rpx;
					background-color: #ff5858;
					display: flex;
					align-items: center;
					justify-content: center;
					color: #fff;
					font-size: 28rpx;
					font-weight: bold;
				}
				
				.btn2 {
					width: 150rpx;
					height: 64%;
					border-radius: 0 34rpx 34rpx 0;
					background-color: #748aff;
					display: flex;
					align-items: center;
					justify-content: center;
					color: #fff;
					font-size: 28rpx;
					font-weight: bold;
				}
			}
		}
		.tip{
			width: 550rpx;
			height: 420rpx;
			border-radius: 15px;
			display: flex;
			flex-direction: column;
			align-items: center;
			.tipImg{
				margin-top: 20rpx;
				height: 300rpx;
				width: 400rpx;
				background-image: url("../../static/home/chatting.png");
				background-size: contain;
				background-repeat: no-repeat;
			}
			.tipWord{
				height: 50rpx;
				width: inherit;
				display: flex;
				justify-content: center;
				align-items: center;
				font-size: 32rpx;
				color: #878787;
			}
		}
		.infos{
			width: 550rpx;
			height: 420rpx;
			border-radius: 15px;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			align-items: center;
			.infoContent{
				margin-top: 60rpx;
				height: 100rpx;
				width: 400rpx;
				display: flex;
				justify-content: space-between;
				align-items: center;
				.infoImg{
					height: 110rpx;
					width: 150rpx;
					background-image: url("../../static/home/interview.jpg");
					background-size: contain;
					background-repeat: no-repeat;
				}
				.infoDetail{
					height: 200rpx;
					width: 250rpx;
					display: flex;
					justify-content: center;
					align-items: center;
					font-size: 32rpx;
					color: #878787;
				}
			}
			
			.infoTitle{
				height: 100rpx;
				width: inherit;
				display: flex;
				justify-content: center;
				align-items: center;
				font-size: 36rpx;
				color: #878787;
				
			}
			.infoWord{
				height: 120rpx;
				width: inherit;
				display: flex;
				justify-content: center;
				align-items: center;
				font-size: 32rpx;
				color: #878787;
				margin-top: 35rpx;
				
			}
		}
	}
</style>
