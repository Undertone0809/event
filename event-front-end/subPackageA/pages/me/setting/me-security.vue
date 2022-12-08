<template>
	<!-- 基本信息卡片 -->
	<view class="flex-col group-card">
		<view class="flex-col card">
			
			<u-modal :show="showEmail" negativeTop=200 :title="emailTitle" showCancelButton=true @confirm="validate()" @cancel="showEmail=false">
				<view class="pop-code">
					<view class="input">
						<input :placeholder="emailHolder" border="surround" v-model="emailCode" type='text' />
					</view>
					<u-code ref="emailValid" @change="emailCodeChange()" change-text="X S" uniqueKey="email"
						@start="emailDisabled = true" @end="emailDisabled = false"></u-code>
					<view class="btn">
						<u-button type="primary" @tap="getEmailCode()" :text="emailTips" :loading="emailLoading"
							:disabled="emailDisabled"></u-button>
					</view>
				</view>
			</u-modal>
			<view @click="bindEmail()" class="justify-between editable">
				<text class="text-title">邮箱</text>
				<view class="flex-row value">
					<text>{{email?email:"未绑定"}}</text>
					<image
						src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423940769096620461.png"
						class="image image_2" />
				</view>
			</view>
			<view @click="bindWx()" class="justify-between editable">
				<text class="text-title">微信</text>
				<view class="flex-row value">
					<text>{{isWxLogin?wxName:"未绑定"}}</text>
					<image
						src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423940769096620461.png"
						class="image image_2" />
				</view>
			</view>
			<u-modal :show="showPsw" negativeTop=200 :title="emailTitle" showCancelButton=true @confirm="validateForPsw()" @cancel="showPsw=false">
				<view class="pop-code">
					<view class="input">
						<input type="text" :placeholder="emailHolder" border="surround" v-model="emailCode"/>
					</view>
					<u-code ref="emailValid" @change="emailCodeChange()" change-text="X S" uniqueKey="email"
						@start="emailDisabled = true" @end="emailDisabled = false"></u-code>
					<view class="btn">
						<u-button type="primary" @tap="getEmailCode()" :text="emailTips" :loading="emailLoading"
							:disabled="emailDisabled"></u-button>
					</view>
				</view>
			</u-modal>
			<view class="justify-between unedit">
				<text class="text-title">密码</text>
				<view class="flex-row psw">
					<image
						src="https://project-user-resource-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423940789572872455.png"
						class="image_3" />
					<image
						src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423940769031105603.png"
						class="image_5" />
					<image
						src="https://project-user-resource-1256085488.cos.ap-guangzhou.myqcloud.com/61e126245a7e3f0310026522/61e1265129d8f60011bbafc3/16423940789572872455.png"
						class="image_3 image_6" />
					<text @click="changePsw()" class="text_7">Change</text>
				</view>
			</view>
			
			<view @click="goGoodbye()" class="justify-between unedit">
				<text class="text-title">注销帐号</text>
			</view>
		</view>
	</view>
</template>

<script>
	import {sendEmailCode,validEmailCode,updateUser} from "@/common/API.js"
	export default {
		data() {
			return {
				email: '',
				showEmail: false,
				emailDisabled: false,
				emailCode: '',
				emailTitle: '绑定邮箱',
				emailHolder:'请输入邮箱',
				emailLoading: false,
				emailTips: '',
				wxName:'',
				showPsw: false,
				psw: '',
				nickName: '',
				isWxLogin:false
			};
		},
		onLoad() {
			let i = uni.getStorageSync('userInfo')
			this.email = i.email
			this.psw = i.eduPassword
			this.wxName = uni.getStorageSync('userInfo').openname;
			this.isWxLogin=uni.getStorageSync('isWxLogin')
		},
		methods: {
			goGoodbye() {
				uni.navigateTo({
					url: "goodbye"
				})
			},
			emailCodeChange(text) {
				this.emailTips = text
			},
			getEmailCode() {
				if(!uni.$u.test.email(this.emailCode)) {uni.$u.toast('请输入正确的邮箱！'); return ;}
				if (this.$refs.emailValid.canGetCode) {
					this.emailLoading = true
					sendEmailCode(uni.getStorageSync('userId'),this.emailCode).then(rs => {
						this.emailLoading = false; this.email = this.emailCode;
						this.$refs.emailValid.start();
						this.emailCode = ''; this.emailHolder = '请输入验证码'})
				} else  uni.$u.toast('请等待倒计时结束');
			},
			bindEmail() {
				if(!this.email) this.showEmail=true
				else {
					uni.showModal({
						title:'更改绑定',
						content:'即将发送验证码至现已绑定邮箱进行验证，是否继续操作？',
						success: (res) => {
							if(res.confirm) {
								sendEmailCode(uni.getStorageSync('userId'),this.email)
								this.showEmail = true;
								this.emailTitle = '验证邮箱'
								this.emailHolder = '请输入验证码'
								this.$refs.emailValid.start();
							}
						}
					})
				}
			},
			bindWx(){
				if(!this.wxName){
					wx.getUserProfile({
					  desc: '必须授权才能使用',
					  success:res=>{
						  
					  wx.login({
					    success:(res)=>{
					      if(res.code){
					        
					      }
					    }
					  })
					  },
					  fall:res=>{
					    console.log('失败',res)
					  }
					})
				}else{
					uni.showToast({title:'微信已绑定',icon:'none'})
				}
			},
			validate() {
				if(!this.emailCode || this.emailHolder=='请输入邮箱') {
					uni.showToast({title:'请先获取/输入验证码！',icon:'none'})
					return ;
				}
				validEmailCode(uni.getStorageSync('userId'),this.email,this.emailCode).then(r => {
					let i = uni.getStorageSync('userInfo')
					if(!i.email || this.emailTitle != '验证邮箱') {
						uni.showToast({title:'绑定成功！',icon:'success'})
						this.showEmail = false
						i.email = this.email;
						uni.setStorageSync('userInfo',i)
					}
					else {
						this.$refs.emailValid.reset()
						this.emailTips = '获取验证码'
						this.emailTitle = '绑定新邮箱'
						this.emailHolder = '请输入邮箱'
						this.emailCode = ''
					}
					
				})
			},
			validateForPsw() {
				if(!this.emailCode || this.emailHolder=='请输入邮箱') { uni.$u.toast('请先获取/输入验证码！'); return ;}
				if(this.emailTips != '新密码')
					validEmailCode(uni.getStorageSync('userId'),this.email,this.emailCode).then(r => {
						this.emailHolder = '请输入新密码'
						this.emailTips = '新密码'
						this.emailDisabled = true
						this.emailCode = ''
					})
				else {
					if(this.emailCode.length<8)  uni.$u.toast('密码必须不小于8位数');
					else {
						updateUser({userid:uni.getStorageSync('userId'),eduPassword:this.emailCode})
						uni.showToast({title:'成功！',icon:'success'})
						this.showEmail = false
						let i = uni.getStorageSync('userInfo')
						i.eduPassword = this.emailCode;
						uni.setStorageSync('userInfo',i)
					}
					
				}
				
			},
			changePsw() {
				if(!this.email) uni.showToast({title:'绑定邮箱后才可修改密码',icon:'none'})
				else { 
					this.showPsw = true
					this.emailTitle = '修改密码',
					this.emailCode = this.email
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.group-card {
		padding: 45rpx 12rpx 0rpx 32rpx;

		.card {
			padding-bottom: 2rpx;

			.pop-code {
				width: 550rpx;
				height: 200rpx;
				padding: 82rpx 20rpx;
				display: flex;
				
				.input {
					width: 345rpx;
					height: 82rpx;
					border: #d4d4d4 1px solid;
					margin-right: 19rpx;
					border-radius: 11rpx;
					padding-top: 18rpx;
					padding-left: 10rpx;
				}
				
				.btn {
					width: 184rpx;
					height: 55rpx;
				}
			}

			.text-title {
				color: rgb(17, 17, 17);
				font-size: 28rpx;
				line-height: 40rpx;
				white-space: nowrap;
			}

			.editable {
				padding: 30rpx 14rpx 30rpx 29rpx;
				border-bottom: solid 2rpx rgba(0, 0, 0, 0.1);

				.value {
					color: #5f5f5f;
					font-size: 28rpx;
					font-weight: 400;
					line-height: 44rpx;
					white-space: nowrap;

					.image_2 {
						margin: 14rpx 0 8rpx 25rpx;
					}
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

				.psw {
					margin-top: 10rpx;
					color: rgb(2, 173, 88);
					font-size: 28rpx;
					font-weight: 600;
					line-height: 34rpx;
					white-space: nowrap;

					.image_3 {
						align-self: center;
						width: 44rpx;
						height: 14rpx;
					}

					.image_5 {
						margin-left: 16rpx;
						align-self: center;
						width: 14rpx;
						height: 14rpx;
					}

					.image_6 {
						margin-left: 16rpx;
					}

					.text_7 {
						margin-left: 28rpx;
						margin-right: 19rpx;
					}
				}
			}
		}

		.image {
			width: 12rpx;
			height: 19rpx;
		}
	}
</style>
