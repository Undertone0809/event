<template>
  <!-- 发布组队 -->
	<view class="page-all">
		<!-- 时间选择器 -->
		<view>
			<u-datetime-picker closeOnClickOverlay=true :show="showStart" v-model="startTime" mode="datetime"
				@confirm="confirmStart()" @cancel="showStart=false" @close="showStart=false"></u-datetime-picker>
			<u-datetime-picker closeOnClickOverlay=true :show="showEnd" v-model="endTime" mode="datetime"
				@confirm="confirmEnd()" @cancel="showEnd=false" @close="showEnd=false"></u-datetime-picker>
		</view>

		<view class="title">
			<input v-model="title" class="input" placeholder="请输入标题" placeholder-style="color:#D9D9DA" />
		</view>
		<view class="ppt">
			<view class="tw time">
				<view class="top">
					<view class="dot1"></view>
					<text class="require">时间: </text>
					<view class="btn time-start" @click="showStart=true">
						<text>{{startTimeText}}</text>
					</view>
					<text style="margin-left: 10rpx;">➜</text>
					<view class="btn time-end" @click="showEnd=true">
						<text>{{endTimeText}}</text>
					</view>
				</view>
		
			</view>
			<view class="tw pnum">
				<view class="dot2"></view>
				<text class="require">人数: </text>
				<u-number-box slot="right-icon" v-model="pnum" step="1" :min="1" :integer="true" :showMinus="pnum > 1"
					bgColor="#fff">
					<view slot="minus" class="minus">
						<u-icon name="minus" size="12"></u-icon>
					</view>
					<view slot="plus" class="plus">
						<u-icon name="plus" color="#FFFFFF" size="12"></u-icon>
					</view>
				</u-number-box>
			</view>
			<view class="tw place" style="margin-top: 26rpx;">
				<view class="dot3"></view>
				<text class="require">地点：</text>
				<u--input v-model="place" style="width: 100%;" placeholder="请输入地点,可留空" border="bottom" clearable></u--input>
				<image class="gps" @click="getLocation()" src="https://s2.loli.net/2022/02/13/NsmEhc1IlQxAra6.png" ></image>
			</view>
			<view class="tw psw" style="margin-top: 26rpx;">
				<view class="dot4"></view>
				<text class="require">申请码：</text>
				<u-code-input v-model="applyCode" maxlength=4 mode="line"></u-code-input>
				<view class="tips" @click="showTip=!showTip">
					<text>?</text>
					<view class="bubble" v-show="showTip">
						<text>仅发布者可见，防止有人随意加入，建议不要公开，加入者会向发布者索取，注意看私信</text>
					</view>
				</view>
			</view>
			<view class="tw place" style="margin-top: 26rpx;">
				<view class="dot3"></view>
				<text class="require">联系方式：</text>
				<u--input v-model="contact" style="width: 100%;" placeholder="请输入联系方式,例: QQ 123456" border="bottom" clearable></u--input>
			</view>
			<view class="select" v-if="close==true">
				<scroll-view scroll-x="true" >
					<view class="noSelect" v-if="onSelectTitle=='未选择比赛'">
						<u-tag :text="onSelectTitle" plain plainFill color="#c9cacc" bgColor="#f1f0f1" borderColor="#c2c2c3"></u-tag>
					</view>
					<view class="tags" v-else>
						<u-tag :text="onSelectTitle" type="error" plain plainFill closable :show="close" @close="cancel()"></u-tag>
					</view>
				</scroll-view>
			</view>
			<view class="bar" v-if="onSelectTitle=='未选择比赛'">
				<scroll-view scroll-x="true" >
					<view class="tags">
						<view v-for="(item,index) in contestList" style="margin-right: 30rpx;">
							<u-tag :text="item.contestTitle" type="error" plain plainFill  @click="select(item)"></u-tag>
						</view>
					</view>
				</scroll-view>
			</view>
		</view>
		<view class="detail">
			<textarea class="textarea" v-model="detail" placeholder="请输入详细需求" maxlength="-1"
				placeholder-style="color:#D9D9DA"></textarea>
		</view>
		<u-upload :fileList="imgList" width="60" height="60" maxSize="2097152" @afterRead="afterRead" @delete="deletePic" name="1" :multiple="true" :maxCount="10">
		</u-upload>
		<view style="width: 100%;height: 200rpx;">
			
		</view>
		<view v-if="showBottom" class="bottom">
			<view @click="goBack()" class="back">
				<u-icon name="play-left-fill"></u-icon>
			</view>
			<view @click="sendIt()" class="flex-col items-center justify-center send-btn">
				<text>发布</text>
			</view>
		</view>
		<view v-if="item" class="bottom">
			<view @click="updateIt()" class="flex-col items-center justify-center send-btn" style="width: 100%;margin-left: 0;">
				<text>更新</text>
			</view>
		</view>
	</view>
</template>

<script>
	import {uploadFile,createTeam,updateTeam,getAllContest,addContestTeam} from "@/common/API.js"
	export default {
		props:['item'],
		data() {
			return {
				showBottom: this.$store.state.sendPageStatus,
				showStart: false,
				showEnd: false,
				title: '',
				detail: '',
				startTime: Number(new Date()),
				endTime: Number(new Date()),
				startTimeText: '选择起始时间',
				endTimeText: '选择终止时间',
				pnum: 1,
				place: '',
				contact:'',
				imgList: [],
				applyCode:'',
				showTip: false,
				contestList:[],
				close:true,
				onSelectTitle:'未选择比赛',
				onSelectId:''
			};
		},
		// #ifdef MP-WEIXIN
		async onTabItemTap() {
			await uni.hideTabBar({animation:true})
			setTimeout(() => {
				this.showBottom = true
			}, 300)
			this.showBottom=true
		},
		// #endif
		// #ifdef MP-QQ
		onShow() {
			uni.hideTabBar({animation:true})
			setTimeout(() => {
				this.showBottom = true
			}, 300)
			
		},
		// #endif
		mounted() {
			var t = this.item
			if(t) {
				this.title = t.teamTitle;
				this.detail = t.teamDetail;
				this.startTime = Number(new Date(t.teamStartTime))
				this.endTime = Number(new Date(t.teamEndTime))
				this.startTimeText = uni.$u.timeFormat(this.startTime, 'yyyy-mm-dd hh:MM');
				this.endTimeText = uni.$u.timeFormat(this.endTime, 'yyyy-mm-dd hh:MM');
				this.pnum = t.teamPnum;
				if(t.teamPlace) this.place = t.teamPlace;
				if(t.teamImgUrl) {
					var rs=[]
					t.teamImgUrl.split(' ').forEach(item => {rs.push({url:item})})
					this.imgList = rs
				}
				this.applyCode = t.teamPsw;
			}
			getAllContest().then(rs => {
				this.contestList = rs;
				console.log(rs)
			},err=>{
				console.log(err)
			})
		},
		methods: {
			init() {
				this.showStart = false;
				this.showEnd = false;
				this.title = '';
				this.detail = '';
				this.startTime = Number(new Date());
				this.endTime = Number(new Date());
				this.startTimeText = '选择起始时间';
				this.endTimeText = '选择终止时间';
				this.pnum = 1;
				this.place = '';
				this.imgList = [],
				this.applyCode = ''
			},
			confirmStart(t) {
				var stime = uni.$u.timeFormat(t.value, 'yyyy-mm-dd hh:MM');
				if (this.endTimeText !== '选择终止时间' && stime > this.endTimeText) {
					uni.showToast({
						title: '开始时间得小于结束时间!',
						icon: 'none'
					})
					return;
				}
				this.startTimeText = stime;
				this.showStart = false;
			},
			confirmEnd(e) {
				var etime = uni.$u.timeFormat(e.value, 'yyyy-mm-dd hh:MM');
				if (this.startTimeText !== '选择起始时间' && etime < this.startTimeText) {
					uni.showToast({
						title: '结束时间得大于开始时间!',
						icon: 'none'
					})
					return;
				}
				this.endTimeText = etime;
				this.showEnd = false;
			},
			getLocation() {
				const that = this
				uni.getLocation({
				    type: 'gcj02', //返回可以用于uni.openLocation的经纬度
				    success: function (res) {
				        const latitude = res.latitude;
				        const longitude = res.longitude;
				        uni.chooseLocation({
				            latitude: latitude,
				            longitude: longitude,
				            success: info => {that.place = info.address}
				        });
				    }
				});
			},
			async sendIt() {
				if(!this.title || !this.detail) {uni.$u.toast('标题和详情均不可为空');return;}
				if(this.startTimeText.includes('选择') || this.endTimeText.includes('选择')) {uni.$u.toast('请选择时间');return;}
				if(this.applyCode.length!=4) {uni.$u.toast('请正确输入申请码');return;}
				uni.showLoading({title:'请稍后'})
				this.$store.commit("setFresh", true)
				var urls = ''
				console.log(this.imgList);
				await this.imgList.forEach((item,index)=>{
					urls = urls+item.url
					if(index!=this.imgList.length-1) urls+=' '
				})
				var info = {
					teamTitle:this.title,
					teamLeader:uni.getStorageSync('userId'),
					teamDetail:this.detail,
					teamStartTime:this.startTimeText+':00',
					teamEndTime:this.endTimeText+':00',
					teamPnum:this.pnum,
					teamPlace:this.place,
					teamPsw:this.applyCode,
					teamImgUrl:urls,
					teamContact:this.contact
				}
				console.log(info)
				createTeam(info).then(r => {
					uni.hideLoading()
					console.log(r)
					addContestTeam(r.teamId,this.onSelectId).then(res=>{
						uni.$u.toast('发布成功！')
						this.$disp.fire(`freshTeam`)
						this.goBack()
					},err=>{
						console.log(err)
					})
				},err=>{
					console.log(err)
				})
			},
			async updateIt() {
				if(!this.title || !this.detail) {uni.$u.toast('标题和详情均不可为空');return;}
				if(this.applyCode.length!=4) {uni.$u.toast('请正确输入申请码');return;}
				uni.showLoading({title:'请稍后'})
				var urls = ''
				await this.imgList.forEach((item,index)=>{
					urls = urls+item.url
					if(index!=this.imgList.length-1) urls+=' '
				})
				var info = {
					teamId:this.item.teamId,
					teamTitle:this.title,
					teamLeader:uni.getStorageSync('userId'),
					teamDetail:this.detail,
					teamStartTime:this.startTimeText+':00',
					teamEndTime:this.endTimeText+':00',
					teamPnum:this.pnum,
					teamPlace:this.place,
					teamPsw:this.applyCode,
					teamImgUrl:urls
				}
				updateTeam(info).then(r => {
					let pages = getCurrentPages()
					let before = pages[pages.length-2]
					before.onPullDownRefresh()
					uni.hideLoading()
					uni.$u.toast('更新成功！')
					setTimeout(()=>{
						uni.navigateBack()
					},700)
				})
			},
			goBack() {
				// this.$store.commit('setSendPageStatus',false)
				setTimeout(() => {
					this.init();
					//uni.showTabBar({animation:true});
					uni.switchTab({url: '/pages/home/home'});
				}, 300)
			},
			/* 图片上传相关函数 */
			deletePic(event) {	// 删除图片
				this.imgList.splice(event.index, 1)
			},
			async afterRead(event) {	// 新增图片
				// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				let imgListLen = this.imgList.length
				lists.map((item) => {
					this.imgList.push({
						...item,
						status: 'uploading',
						message: '上传中'
					})
				})
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url)
					let item = this.imgList[imgListLen]
					this.imgList.splice(imgListLen, 1, Object.assign(item, {
						status: 'success',
						message: '',
						url: result
					}))
					imgListLen++
				}
			},
			uploadFilePromise(url) {
				return new Promise((resolve, reject) => {
					let a = uploadFile(url).then(res=>{
						 resolve(res)
					})
				})
			},
			select(item){
				this.onSelectTitle=item.contestTitle;
				this.onSelectId=item.contestId;
			},
			cancel(){
				this.onSelectTitle='未选择比赛';
				this.onSelectId='';
			}
		}
	}
</script>

<style lang="scss" scoped>

	.page-all {
		margin: 10rpx 30rpx 20rpx;

		.title {
			height: 60px;

			.input {
				min-height: 60px;
				height: 60px;
				border-bottom: 2px solid #e5eaf0;
				font-size: 25px;
			}
		}

		.detail {
			margin-top: 60rpx;
			width: 100%;
			.textarea {
				width: 100%;
				height: 488rpx;
			}
		}

		.ppt {
			width: 100%;
			border-top: 2px solid #e5eaf0;

			.tw {
				margin-top: 38rpx;
			}

			.time {
				.btn {
					height: 54rpx;
					display: inline-flex;
					line-height: 10rpx;
					margin-left: 7rpx;
					border-radius: 25rpx;
					font-size: 27rpx;
					color: #ffffff;
					justify-content: center;
					align-items: center;
					padding: 0 20rpx;
					background: #22d019;
					font-weight: 600;
				}

				.top {
					display: flex;
					align-items: center;

					.dot1 {
						margin-right: 14rpx;
						width: 16rpx;
						border-radius: 50%;
						height: 16rpx;
						background-color: rgb(255, 113, 113);
						display: inline-block;
					}
				}

				.time-start {
					background: #22d019;
				}

				.time-end {
					background: #ff4b65;
				}

			}

			.pnum {
				display: flex;
				align-items: center;

				.dot2 {
					margin-right: 14rpx;
					width: 16rpx;
					border-radius: 50%;
					height: 16rpx;
					background-color: rgb(222, 127, 255);
					display: inline-block;
				}

				.minus {
					width: 22px;
					height: 22px;
					border: 1px #cbcdcd solid;
					border-radius: 50%;
					display: flex;
					justify-content: center;
					align-items: center;
					margin-left: 30rpx;
				}

				.plus {
					width: 22px;
					height: 22px;
					background-color: #FF0000;
					border-radius: 50%;
					display: flex;
					justify-content: center;
					align-items: center;
				}
			}

			.place {
				display: flex;
				align-items: center;
				
				.dot3 {
					margin-right: 14rpx;
					// margin-top: 18rpx;
					width: 16rpx;
					border-radius: 50%;
					height: 16rpx;
					background-color: rgb(83, 195, 255);
					display: inline-block;
				}
				
				.gps {
					width: 63rpx;
					height: 68rpx;
					margin-left: 27rpx;
				}
			}
			.select{
				height: 80rpx;
				display: flex;
				align-items: center;
				.noSelect{
					display: flex;
					align-items: center;
					height: 100rpx;
					width: 300rpx;
					margin-top: 10rpx;
				}
				.tags{
					display: flex;
					align-items: center;
					height: 100rpx;
					width: 300rpx;
					.tag{
						padding: 10rpx;
						border-radius: 20rpx;
						border: 1rpx solid #ff4b65;
						color: #ff4b65;
						font-size: 24rpx;
						margin-right: 30rpx;
					}
				}
			}
			.bar{
				height: 80rpx;
				display: flex;
				align-items: center;
				.tags{
					display: flex;
					align-items: center;
					height: 100rpx;
					width: 300rpx;
					margin-top:20rpx;
					.tag{
						padding: 10rpx;
						border-radius: 20rpx;
						border: 1rpx solid #ff4b65;
						color: #ff4b65;
						font-size: 24rpx;
						margin-right: 30rpx;
					}
				}
			}
			
			.psw {
				display: flex;
				align-items: center;
				
				.dot4 {
					margin-right: 14rpx;
					// margin-top: 18rpx;
					width: 16rpx;
					border-radius: 50%;
					height: 16rpx;
					background-color: #ffde7a;
					display: inline-block;
				}
				.tips {
					width: 40rpx;
					height: 40rpx;
					border-radius: 50%;
					border: 1px solid #adadad;
					margin-left: 157rpx;
					color: #adadad;
					font-weight: 300;
					display: flex;
					align-items: center;
					align-content: center;
					justify-content: center;
					
					.bubble {
						//气泡框的主体是一个矩形
						padding-top: 11rpx;
						padding-left: 20rpx;
						position: absolute;
						width: 400rpx;
						padding-right: 3px;
						background-color: #e0dadd;
						height: 146rpx;
						top: 530rpx;
						right: 30rpx;
						z-index: 100;
						box-shadow: 0 2px 16px 0 rgba(0, 0, 0, 0.08);
						border-radius: 14rpx;
						font-size: 28rpx;
						font-weight: 500;
					}
				
					.bubble:before,
					.bubble:after {
						//加三角形
						content: "";
						display: block;
						position: absolute;
						bottom: 72px;
						right: 3px;
						//别给宽高
						border: 7px solid transparent; 
						border-color: transparent transparent #e0dadd transparent; //上为白
						z-index: 0;
					}
				
					.bubble:after {
						//用来给三角形加边框
						bottom: -42px;
						border-color: transparent transparent #fff;
						z-index: 1;
					}
				}
			}

			.require {
				color: rgb(83, 83, 83);
				font-size: 32rpx;
			}
		}

		@keyframes anim { 
			0% {opacity: 0};
			100%{opacity:1};
		};
		.bottom {
			width: 100%;
			height: 130rpx;
			display: flex;
			align-items: center;
			position: fixed;
			top: 90vh;
			background-image: linear-gradient(360deg, #ffffff 0%, #ffffff 85%);
			padding-bottom: 30rpx;
			padding-right: 51rpx;
			animation: anim 0.5s;

			.back {
				border-radius: 50%;
				width: 96rpx;
				height: 96rpx;
				background-color: #D2E1E5;
				color: #7d8487;
				display: flex;
				justify-content: center;
				align-items: center;
			}

			.send-btn {
				color: #fff;
				background-image: linear-gradient(79.7deg,
						rgb(35, 188, 186) 0%,
						rgb(35, 188, 186) -20.71%,
						rgb(69, 233, 148) 120.71%,
						rgb(69, 233, 148) 100%);
				border-radius: 48rpx;
				height: 96rpx;
				width: 78%;
				margin-left: 50rpx;
			}
		}
	}
</style>
