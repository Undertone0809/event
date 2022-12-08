<template>
	<view class="flex-col page-all ">
		<!-- #ifdef MP-QQ -->
		<modal-qq :show="showModal" :title='title' v-model="codeValue" :placeholder='placeholder' @confirm="confirmCode()" @cancle="showModal=false,codeValue='',showTextarea = true"></modal-qq>
		<!-- #endif -->

		<!-- 头像 -->
		<view class="flex-col items-center group-avatar">
			<image :src="userInfo.avatar" class="avatar" />
			<!-- #ifdef MP-WEIXIN -->
			<cropper class="avatar-change" selWidth="660rpx" selHeight="660rpx" @upload="changeAvatar()" avatarSrc="https://project-user-resource-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423940789576298834.png" avatarStyle="width:68rpx;height:68rpx;"></cropper>
			<!-- #endif -->
			<!-- #ifdef MP-QQ -->
			<image @click="chooseAvatar()" class="avatar-change" src="https://project-user-resource-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423940789576298834.png"></image>
			<!-- #endif -->
		</view>
		<!-- 基本信息卡片 -->
		<view class="flex-col group-card ">
			<view class="flex-col card">
				<view @click="changeName()" class="justify-between editable">
					<text class="text-title">昵称</text>
					<view class="flex-row value">
						<text>{{userInfo.openname}}</text>
						<image
							src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423940769096620461.png"
							class="image image_2" />
					</view>
				</view>
				<view class="justify-between unedit">
					<text class="text-title">账号</text>
					<text class="value">{{userInfo.eduAccount || userInfo.email}}</text>
				</view>
			</view>
		</view>

		<!-- 个人简介 -->
		<view v-show="showTextarea" class="flex-col group-card">
			<view class="flex-col card">
				<view class="editable">
					<p class="text-title" style="margin-bottom: 30rpx;">自我评价</p>
					<u--textarea v-model="userInfo.userResume" placeholder="请输入自我评价(如：这个人很懒，什么也没留下)" count></u--textarea>
				</view>
			</view>
			<view class="flex-col card">
				<view class="editable">
					<p class="text-title" style="margin-bottom: 30rpx;">教育经历</p>
					<u--textarea v-model="userInfo.userEdu" placeholder="请描述教育经历(如：华东交通大学)" count></u--textarea>
				</view>
			</view>
			<view class="flex-col card">
				<view class="editable">
					<p class="text-title" style="margin-bottom: 30rpx;">项目经验</p>
					<u--textarea v-model="userInfo.userExpr" placeholder="请描述项目经验(可选)" count></u--textarea>
				</view>
			</view>
		</view>
		<!-- #ifdef MP-QQ -->
		<view v-show="!showTextarea"><view style="width: 750rpx;height: 968rpx;"></view></view>
		<!-- #endif -->

		<view class="flex-col group-card">
			<!-- 擅长技能卡片 -->
			<view class="flex-col card">
				<view class="editable">
					<p @click="addSkill()" class="text-title" style="margin-bottom: 30rpx;">擅长技能
						<view class="add-btn">+</view>
					</p>
					
					<view>
						<view v-for="(item,index) in skill" :key="index">
							<text>{{item}}</text><view class="remove" @click="removeSkill(item,index)">-</view>
						</view>
					</view>
					
				</view>
			</view>
			<!-- 获奖卡片 -->
			<view class="flex-col card">
				<view class="editable">
					<p @click="addPrize()" class="text-title" style="margin-bottom: 30rpx;">获奖及荣誉
						<view class="add-btn">+</view>
					</p>
					
					<view>
						<view v-for="(item,index) in prize" :key="index">
							<text>{{item}}</text><view class="remove" @click="removePrize(item,index)">-</view>
						</view>
					</view>

				</view>
			</view>
		</view>

		<!-- 底部空间留给按钮 -->
		<view style="width: 100%;height: 300rpx;"></view>
		
		<!-- 按钮 -->
		<view class="button-group" v-show="showTextarea">
			<view @click="saveAll()" class="flex-col items-center button-save">
				<text>保存修改</text>
			</view>
			<!-- #ifndef MP-QQ -->
			<view @click="goBack()" class="flex-col items-center button-logout">
				<text>返回</text>
			</view>
			<!-- #endif -->
		</view>
	</view>
</template>

<script>
	import {getUserIntro,updateUser,uploadFile,updateIntro} from "@/common/API.js"
	import modalQq from "../../components/common/modal-qq.vue"
	import cropper from "@/subPackageA/components/me/cropper.vue"
	export default {
		components: { cropper,modalQq },
		data() {
			return {
				showTextarea: true,
				userInfo:{},
				skill: [],
				prize: [],
				// #ifdef MP-QQ
				showModal:false,
				codeValue:'',
				title:'',
				placeholder:''
				// #endif
			};
		},
		onLoad() {
			this.userInfo = uni.getStorageSync('userInfo')			
			this.skill = this.userInfo.userSkill.split('^').slice(1)
			this.prize = this.userInfo.userPrize.split('^').slice(1)
		},
		methods: {
			// #ifdef MP-QQ
			chooseAvatar() {
				uni.chooseImage({
					count:1,
					success: (res) => {
						var tempUrl = res.tempFilePaths[0]
						this.userInfo.avatar = tempUrl
						uni.$u.toast('头像已修改')
						uploadFile(tempUrl).then(url => {
							this.userInfo.avatar = url
							uni.setStorageSync('userInfo',this.userInfo)
							updateUser({userid:this.userInfo.userid,avatar:url}).then(rs => {console.log(res);})
						})
					}
				})
			},
			// #endif
			changeAvatar(e) {
				var tempUrl = e.path
				this.userInfo.avatar = tempUrl
				uni.$u.toast('头像已修改')
				uploadFile(tempUrl).then(url => {
					this.userInfo.avatar = url
					uni.setStorageSync('userInfo',this.userInfo)
					updateUser({userid:this.userInfo.userid,avatar:url})
				})
			},
			changeName() {
				// #ifdef MP-QQ
				this.showTextarea = false; this.showModal = true; this.title = '修改昵称'; this.placeholder = this.userInfo.openname
				// #endif
				// #ifdef MP-WEIXIN
				uni.showModal({
					title:'修改昵称', editable:true, placeholderText:this.userInfo.openname,
					success: (e) => { 
            if(e.confirm){
              let content = this.$u.trim(e.content)
              if(content) this.userInfo.openname = content
              else uni.showToast({title:'昵称不能为空',icon:'none'})
            }
          }
				})
				// #endif
			},
			addSkill(){
				// #ifdef MP-QQ
				this.showTextarea = false; this.showModal = true; this.title = '添加擅长技能'; this.placeholder = '精通/熟悉/了解某技术'
				// #endif
				// #ifdef MP-WEIXIN
				uni.showModal({
					title:'添加擅长技能', editable:true, placeholderText:'精通/熟悉/了解某技术',
					success: (e) => { 
						if(e.confirm) {
							this.skill.push(e.content)
							this.userInfo.userSkill+='^'+e.content
						} }
				})
				// #endif
			},
			addPrize(){
				// #ifdef MP-QQ
				this.showTextarea = false; this.showModal = true; this.title = '添加获奖及荣誉'; this.placeholder = '例如全国挑战杯一等奖'
				// #endif
				// #ifdef MP-WEIXIN
				uni.showModal({
					title:'添加获奖及荣誉', editable:true, placeholderText:'例如全国挑战杯一等奖',
					success: (e) => { 
						if(e.confirm) {
							this.prize.push(e.content)
							this.userInfo.userPrize+='^'+e.content
						} }
				})
				// #endif
			},
			removeSkill(item,idx) {
				this.skill.splice(idx,1);
				this.userInfo.userSkill = this.userInfo.userSkill.replace('^'+item,'')
			},
			removePrize(item,idx) {
				this.prize.splice(idx,1);
				this.userInfo.userPrize = this.userInfo.userPrize.replace('^'+item,'')
			},
			// #ifdef MP-QQ
			confirmCode() {
				this.showModal = false
				this.showTextarea = true
				if(this.title == '修改昵称') this.userInfo.openname = this.codeValue
				else if(this.title == '添加擅长技能') {
					this.skill.push(this.codeValue)
					this.userInfo.userSkill+='^'+this.codeValue
				}
				else {
					this.prize.push(this.codeValue)
					this.userInfo.userPrize+='^'+this.codeValue
				}
				this.codeValue = ''
			},
			// #endif
			goBack() {
				uni.navigateBack()
			},
			saveAll() {
        // 判断edu是否以'大学'结尾
        if(!this.$u.test.isEmpty(this.userInfo.userEdu) && !this.userInfo.userEdu.endsWith('大学')){
          uni.showToast({title:'学历必须为:xx大学',icon:'none'})
          return
        }
        if(!this.$u.test.isEmpty(this.userInfo.userEdu) && this.$u.test.number(this.userInfo.userResume)){
          uni.showToast({title:'怎么自我评价都是数字',icon:'none'})
          return
        }
        // modify cache
				uni.setStorageSync('userInfo',this.userInfo)
				uni.showLoading({title:'保存中'})
				updateUser(this.userInfo)
				updateIntro(this.userInfo).then(r => {
					setTimeout(() => { this.$disp.fire(`event.fresh_page_me`) }, 500)
          uni.hideLoading()
					uni.$u.toast('保存成功!')	
					uni.navigateBack()
				})
			}
		}
	};
</script>

<style scoped lang="scss">
	.page-all {
		padding: 72rpx 38rpx 32rpx 16rpx;
		width: 100%;
		height: 100%;
		overflow-y: scroll;
		
		.group-avatar {
			align-self: center;
			border-radius: 200rpx;
			width: 220rpx;
			position: relative;

			.avatar {
				border-radius: 50%;
				width: 220rpx;
				height: 220rpx;
			}

			.avatar-change {
				border-radius: 34rpx;
				width: 68rpx;
				height: 68rpx;
				position: absolute;
				right: -2rpx;
				bottom: 6rpx;
			}
		}

		.button-group {
			position: fixed;
			bottom: 0rpx;
			width: 95%;
			z-index: 99;
			background-color: #fff;
			padding-bottom: 40rpx;

			.button-save {
				padding: 28rpx 0;
				color: rgb(255, 255, 255);
				font-size: 32rpx;
				font-weight: 600;
				line-height: 44rpx;
				white-space: nowrap;
				background-image: linear-gradient(360deg, #fd6b76 0%, #f62c1f 85%);
				border-radius: 16rpx;
				z-index: 9;
			}

			.button-logout {
				margin-top: 12rpx;
				border: solid 4rpx rgba(0, 0, 0, 0.05);
				padding: 25rpx 0;
				color: rgb(0, 0, 0);
				font-size: 32rpx;
				font-weight: 600;
				line-height: 44rpx;
				white-space: nowrap;
				background-image: linear-gradient(360deg, #ffffff 0%, #ffffff 85%);
				border-radius: 16rpx;
				z-index: 99;

			}
		}

		.group-card {
			padding: 45rpx 12rpx 0rpx 32rpx;

			.card {
				padding-bottom: 2rpx;

				.text-title {
					color: rgb(17, 17, 17);
					font-size: 28rpx;
					line-height: 40rpx;
					white-space: nowrap;
					font-weight: 500;
				}

				.editable {
					padding: 30rpx 14rpx 30rpx 29rpx;
					border-bottom: solid 2rpx rgba(0, 0, 0, 0.1);

					.value {
						color: #5f5f5f;
						font-size: 32rpx;
						font-weight: 600;
						line-height: 44rpx;
						white-space: nowrap;

						.image_2 {
							margin: 6rpx 0 8rpx 20rpx;
						}
					}

					.add-btn {
						display: inline-block;
						margin-left: 19rpx;
						padding: 8rpx 0 10rpx;
						color: #414141;
						font-size: 39rpx;
						font-weight: 600;
						line-height: 17rpx;
						white-space: nowrap;
						background-color: white;
						border-radius: 29rpx;
						width: 43rpx;
						height: 43rpx;
						border: solid 2rpx #e4e4e4;
						text-align: center;
					}
					
					.remove {
						display: inline-block;
						margin-left: 19rpx;
						padding: 8rpx 0 10rpx;
						color: #fc3f37;
						font-size: 39rpx;
						font-weight: 600;
						line-height: 17rpx;
						white-space: nowrap;
						background-color: white;
						border-radius: 29rpx;
						width: 43rpx;
						height: 43rpx;
						border: solid 2rpx #e4e4e4;
						text-align: center;
					}
				}

				.unedit {
					padding: 32rpx 0 32rpx 32rpx;
					border-bottom: solid 2rpx rgba(0, 0, 0, 0.1);

					.value {
						color: #5f5f5f;
						font-size: 28rpx;
						font-weight: 600;
						line-height: 40rpx;
						white-space: nowrap;
					}
				}
			}

			.image {
				width: 18rpx;
				height: 30rpx;
			}
		}
	}
</style>
