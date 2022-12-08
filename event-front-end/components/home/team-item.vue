<template>
	<view class="flex-col">
		<view class="flex-col post">
			<view class="top justify-between">
				<view @click="goUser()" class="flex-row">
					<image :src="item.avatar" class="avatar" />
					<view class="flex-col info">
						<text v-if="hasDistance" class="user-intro">{{'距离您'+item.distance+'km' || '@none'}}</text>
            <text v-else class="user-intro">{{item.email || '@none'}}</text>
						<text class="user-name">{{item.openname}}</text>
					</view>
				</view>
				<text class="send-time">{{ new Date(item.teamSendTime).valueOf() | timeFrom }}</text>
			</view>
			<view class="tag" v-if="teamData.userEdu!==''&&teamData.userEdu==userEdu&&teamData.teamLeader!==userId||
			leaderEdu!==''&&leaderEdu==userEdu&&teamData.teamLeader!==userId||teamData.contestTitle!=null&&teamData.contestTitle!=''">
				<view class="tagItem">
					<view v-if="teamData.userEdu!==''&&teamData.userEdu==userEdu&&teamData.teamLeader!==userId||
					leaderEdu!==''&&leaderEdu==userEdu&&teamData.teamLeader!==userId" style="margin-right: 10rpx;">
						<u-tag text="与我同校" plainFill  type="warning" shape="circle" size="mini" plain></u-tag>
					</view>
					<view v-if="teamData.contestTitle!=null&&teamData.contestTitle!=''">
						<u-tag :text="'#'+teamData.contestTitle" color="#276bf4" plainFill shape="circle" size="mini" plain @click="toContest(teamData.contestId)"></u-tag>
					</view>
				</view>
			</view>
			<view v-else class="bar" style="width: 100%;height: 25rpx;"></view>
			<view @click="goDetail()" class="flex-col post-content">
				<view class="top flex-col">
					<text class="title">{{item.teamTitle}}</text>
					<text class="detail">{{item.teamDetail}}</text>
				</view>
				<view class="top ppt">
					<view>
						<view class="dot1"></view>
						<text class="require">时间：{{formatTime(item.teamStartTime)}} ➜ {{formatTime(item.teamEndTime)}} </text>
						<text class="remain" :style="{color:remainColor}">{{remainTime()}}</text>
					</view>
					<view class="tw">
						<view class="dot2"></view>
						<text class="require">人数：{{item.teamGetPnum}}/{{item.teamPnum}}人</text>
					</view>
					<view class="tw" v-if="item.teamPlace">
						<view class="dot3"></view>
						<text class="require">地点：{{item.teamPlace}}</text>
					</view>
				</view>
			</view>
			<view class="album" v-if="item.teamImgUrl">
				<u-album :urls="imgs" singleSize="280" multipleSize="27vw" maxCount="3" space="2" ></u-album>
			</view>
			<view class="flex-row bottom">
				<view class="like" @click="likeIt()">
					<image
						:src="likeImg"
						class="like-img" /> <text :class="{'fill':hasLike==true}">{{likeNum}}</text>
				</view>
				<view class="favor" @click="collectIt()">
					<image 
						:src="collectImg"
						class="favor-img" /> <text :class="{'fill':hasCollect==true}">{{collectNum}}</text>
				</view>

				<view style="margin-right: 32rpx;">
					<image @click="copyTeamCode()"
					src="/static/home/clipboard.png"
					class="copy-img" />
				</view>
				
			</view>
			
		</view>
	</view>
</template>

<script>
	import {getLikeNum,getCollectNum,disLikeTeam,unCollectTeam,likeTeam,collectTeam,hasCollectTeam,hasLikeTeam,getUserById,getUserIntro,getContestById} from '@/common/API.js'
	export default {
		props:{
      teamData:{
        type:Object,
        default(){ return {} }
      },
      hasDistance:{
        type:Boolean,
        default(){ return false }
      }
    },
		data() {
			return {
				item:this.teamData,
				likeNum: '-',
				collectNum: '-',
				likeImg: '/static/home/like.png',
				collectImg:'/static/home/collect.png',
				hasCollect:false,
				hasLike:false,
				remainColor:'#2CC900',
				userEdu:'',
				userId:'',
				leaderEdu:''
			};
		},
		computed:{
			imgs() {
				return this.item.teamImgUrl ? this.item.teamImgUrl.split(' ') : []
			}
		},
		mounted() {
			this.userId=uni.getStorageSync('userId');
			if(this.userId){
				getUserIntro(uni.getStorageSync('userId')).then(rs => {this.userEdu=rs.userEdu})
				hasCollectTeam(uni.getStorageSync('userId'),this.item.teamId).then(rs => {if(rs) this.collectImg = '/static/home/collect-fill.png', this.hasCollect = true})
				hasLikeTeam(uni.getStorageSync('userId'),this.item.teamId).then(rs => {if(rs) this.likeImg = '/static/home/like-fill.png', this.hasLike = true})
			}
			getUserIntro(this.item.teamLeader).then(rs => {this.leaderEdu=rs.userEdu})
			getLikeNum(this.item.teamId).then(rs => {this.likeNum = rs})
			getCollectNum(this.item.teamId).then(rs => {this.collectNum = rs})
			
		},
		methods: {
			copyTeamCode() {
				uni.setClipboardData({
				    data: String(this.item.teamCode),
					showToast: false,
				    success: () => { uni.$u.toast('组队码已复制') }
				});
			},
			async likeIt() {
        if(!uni.getStorageSync('hasLogin')){
          uni.showToast({title:'请在登录后进行操作',icon:'none'})
          return
        }
				if(this.hasLike) {
					disLikeTeam(uni.getStorageSync('userId'),this.item.teamId)
					this.hasLike = false
					this.likeImg = '/static/home/like.png'
					this.likeNum--
				} else {
					await likeTeam(uni.getStorageSync('userId'),this.item.teamId)
					this.hasLike = true
					this.likeImg = '/static/home/like-fill.png'
					this.likeNum++
				}
			},
			async collectIt() {
        if(!uni.getStorageSync('hasLogin')){
          uni.showToast({title:'请在登录后进行操作',icon:'none'})
          return
        }
				if(this.hasCollect) {
					uni.showModal({
						title:'提示',
						content:'确认取消收藏吗？',
						success: (res) => {
						    if (res.confirm) {
								unCollectTeam(uni.getStorageSync('userId'),this.item.teamId)
								this.hasCollect = false
								this.collectImg = '/static/home/collect.png'
								if(this.collectNum!=0)
								this.collectNum--
						    } }
					})
				} else {
					await collectTeam(uni.getStorageSync('userId'),this.item.teamId)
					this.hasCollect = true
					this.collectImg = '/static/home/collect-fill.png'
					this.collectNum++
				}
			},
			goDetail() {
				uni.navigateTo({url:`/subPackageA/pages/home/teamDetail?teamId=${this.item.teamId}&likeNum=${this.likeNum}&hasLike=${this.hasLike}&hasCollect=${this.hasCollect}&teamLeader=${this.item.teamLeader}`})
			},
			goUser() {
				uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+this.item.teamLeader})
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
				if(days == 0) return ''+hours+"小时"+minutes+"分钟"
				return ''+days+"天"+hours+"小时"
			},
			toContest(e){
				console.log(1)
				getContestById(e).then(res=>{
					let contestItem=encodeURIComponent(JSON.stringify(res))
					uni.navigateTo({url:`/subPackageA/pages/home/contestDetail?contestItem=`+contestItem})
				})
				
			}
		}
	};
</script>

<style scoped lang="scss">

	.post {
		margin: 0 25rpx;
		padding: 28rpx 0 2rpx 4rpx;
		background-color: rgb(255, 255, 255);
		box-shadow: 0px 2rpx 8rpx 0px rgba(165, 165, 165, 0.5);
		border-radius: 18rpx;
		
		.top {
			padding: 0 24rpx;

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
				color: rgb(135, 135, 135);
				font-size: 28rpx;
				line-height: 46rpx;
				text-align: left;
				overflow: hidden;
				text-overflow: ellipsis;
				// height: 84rpx;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				/*可指定任一行显示...效果*/
				-webkit-box-orient: vertical;
				/*此时不需要white-space:nowrap;*/
			}
		}
		.tag{
			width: 100%;
			height: 80rpx;
			display: flex;
			align-items: center;
			.tagItem{
				padding: 0 24rpx;
				display: flex;
			}
		}
		.post-content {
			// margin-top: 40rpx;
			.ppt {
				margin-top: 27rpx;

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

				.remain {
					color: rgb(44, 201, 0);
					font-size: 26rpx;
					font-weight: 500;
					line-height: 48rpx;
					margin-left: 10rpx;
				}

				.require {
					color: #535353;
					font-size: 28rpx;
					line-height: 48rpx;
					font-weight: 500;
				}

			}
		}

		.album {
			margin: 20rpx 24rpx;
		}
		.bottom {
			width: 100%;
			height: 60rpx;
			align-items: center;
			justify-content: space-between;
			margin-top: 20rpx;
			padding: 41rpx 41rpx;
			// border-top: #e9ecec solid 1px;

			.like {
				display: flex;

				text {
					margin-left: 10rpx;
					font-size: 30rpx;
					color: #535353;
					line-height: 56rpx;
					font-weight: 700;
				}
				
				.fill {
					color: #31a4ea;
				}

				.like-img {
					width: 56rpx;
					height: 56rpx;
				}
			}

			.favor {
				display: flex;

				text {
					margin-left: 10rpx;
					font-size: 30rpx;
					color: #535353;
					line-height: 56rpx;
					font-weight: 700;
				}
				
				.fill {
					color: #31a4ea;
				}

				.favor-img {
					width: 56rpx;
					height: 56rpx;
				}
			}

			.copy-img {
				width: 64rpx;
				height: 61rpx;
			}
		}


	}
</style>
