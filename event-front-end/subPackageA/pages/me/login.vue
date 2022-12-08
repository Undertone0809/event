<template>	
	<view class="container" :style="{height:phoneInfo.height+'rpx'}" @click="navBack(1)">
		<!-- 顶部导航栏区域 -->
		<view class="topNav">
			<!-- 占位符，状态栏小程序撑起高度 -->
			<view :style="{height:statusBarHeight+'px'}"></view>
			<!-- 该区域放置需要放置的内容 -->
			<view class="topNav-content" :style="{height:navBarHeight+'px'}" >
				<image class="cancel-image" v-if="currentIndex==1||currentIndex==2" src="https://s2.loli.net/2022/02/10/NOZqRHvhkl39jfI.png"  mode="widthFix" @click="turnBack()"></image>
				<view class="topNav-words">
					一问Event
				</view>
			</view>
		</view>
					
		<!-- 需要添加占位符高度  状态栏高度+导航栏高度（否则下面tab会塌陷）-->
		<!-- 如果position:fixed时需要使用该段代码 -->
		<view :style="{height:statusBarHeight+navBarHeight+'px'}"></view>
		
		<!-- 顶部占位符 -->
		<view class="top-container" :style="{height:phoneInfo.topImageHeight+'rpx'}">
			<image class="top-image" v-if="currentIndex==0" src="/static/me/guide1.png" mode="widthFix"></image>
			<image class="top-image" v-if="currentIndex==1" src="/static/me/guide2.png" mode="widthFix"></image>
			<image class="top-image" v-if="currentIndex==2" src="/static/me/guide3.png" mode="widthFix"></image>
		</view>

		<!-- 以下容器放置主要信息 -->
		<view class="info-container" :style="{height:phoneInfo.infoContainerHeight+'rpx'}">
			
			<!-- 页面1 -->
			<view class="stack1-wrap" v-if="currentIndex==0">
				<view class="top-description">
					<text>选择您的操作</text>
				</view>
				<view class="stack1-middle-wrap" @click="showOperate()">
					<view class="stack1-middle-words">
						<text>{{operate}}</text>
					</view>
					<view class="stack1-middle-icon">
						<u-icon class="stack1-middle-icon" name="arrow-down" color="#6c63ff" size="20"></u-icon>
					</view>
				</view>
				<!-- <view class="tips"><text>(小花椒可免注册使用交大帐号登录)</text></view> -->
				<view class="bottom-btn" @click="next()">
					下一步
				</view>
				
			</view>
			
			<!-- 页面2 -->
			<view class="statck2-wrap" v-if="currentIndex==1">
				
				<view class="top-description">
					<text>{{title2}}</text>
				</view>
				
				<view class="stack2-middle-wrap">
						<!-- 帐号 -->
						<view class="stack2-middle-item">
							<view class="stack2-middle-item-icon"><u-icon name="account" color="#6c63ff" size="22"></u-icon></view>
							<input class="stack2-middle-item-input" type="text" v-model="form.account" :placeholder="accountPlaceholder"/>
						</view>
						<!-- 验证码 -->
						<view class="stack2-middle-item" v-if="registerFlag">
							<view class="stack2-middle-item-icon"><u-icon name="email" color="#6c63ff" size="22"></u-icon></view>
							<input class="stack2-middle-item-input" style="width: 312rpx;" type="text" placeholder="请查收邮箱验证码" v-model="form.code"/>
							<u-code seconds=30 change-text="X S" uniqueKey="emailRegst" ref="uCode" @change="codeChange"></u-code>
							<view @click="getCode()" class="code-btn">{{tips}}</view>
						</view>		
						<!-- 密码 -->
						<view class="stack2-middle-item">
							<view class="stack2-middle-item-icon"><u-icon name="lock" color="#6c63ff" size="22"></u-icon></view>
							<input class="stack2-middle-item-input" type="text" :password="!showPwd" :placeholder="pswPlaceholder" v-model="form.psw"/>
							<!-- 查看密码icon -->
							<image v-if="!showPwd" class="stack2-middle-showPwdIcon" src="/static/me/eye_open.png" @click="watchPwd()"></image>
							<image v-else class="stack2-middle-showPwdIcon" src="/static/me/eye_close.png" @click="watchPwd()"></image>
						</view>				
				</view>
				<view class="bottom-btn" @click="submit()">
					开始验证
				</view>
				
			</view>

			<view class="stack3-wrap" v-if="currentIndex==2">
				<view class="stack3-item">
					走进Event
				</view>
				<view class="stack3-item">
					一起享受快乐生活
				</view>
			</view>
			<view class="wxLogin" v-if="currentIndex==0" @click="login()">
				<!-- <u-button type="success" text="微信登录" icon="weixin-fill" :plain="true" @click="login()"></u-button> -->
				<view class="items-center wxWord">
					<u-icon name="weixin-fill" color="#4f4dc8" size="26"></u-icon>微信一键登录
				</view>
			</view>
			<view class="privacy-wrap" :style="{top:prTop+'rpx'}" v-if="!registerFlag">
				进行认证即代表您已同意并且阅读了
				<view class="privacy-nav" @click="navToPrivacy">
					《隐私保护政策》
				</view>
			</view>
			
			<view class="cancel" @click="navBack(0)" v-if="currentIndex!=2">
				暂不登录
			</view>

		</view>

	</view>
</template>

<script>
	import {getUserIntro,getUserEdu,createToken,userLogin,updateUser,userRegister,validRegister,wxLogin,bindWxInfo} from "@/common/API.js"
	export default{
		data(){
			return{
				currentIndex:0,//判断当前页面
				navBarHeight:10,
				statusBarHeight:10,
				phoneInfo: {
					height:10,
					infoContainerHeight:10,
					topImageHeight:10,
					cancelImageTop:10,
				},
				
				accountPlaceholder:"请输入邮箱",//输入框占位符,在选择了学校之后才会初始化
				pswPlaceholder:"请输入密码",//输入框占位符,在选择了学校之后才会初始化
				operate:'登录',
				showPwd:false,
				registerFlag:false,
				pwdType:"password",
				title2:'验证身份信息',
				tips:'',
				prTop:'',
				form: {
					psw: '',
					account: '',
					code:''
				},
				wxUserInfo:{},
				isWxLogin:false,
				loginRes:{}
			}
		},
		onLoad(){
			this.initUI();
			uni.login({
			  success:(res)=>{
			    if(res.code){
					wxLogin(res.code).then(res=>{
						
						this.loginRes=res;
					},err=>{
						console.log(err)
					})
			    }
			  }
			})
		},
		methods:{
			//初始化UI参数
			initUI(){
				//获取手机高度（rpx）
				this.phoneInfo.height = this.$common.commonData.phoneData.phoneInfo.height;
				this.statusBarHeight = this.$common.commonData.phoneData.statusBarHeight;
				this.navBarHeight = this.$common.commonData.phoneData.navBarHeight;
				this.phoneInfo.topImageHeight =  0.30*(this.phoneInfo.height-this.statusBarHeight);
				this.phoneInfo.infoContainerHeight =  0.65*(this.phoneInfo.height-this.statusBarHeight);
				this.phoneInfo.cancelImageTop = this.$common.commonData.phoneData.menuButtonInfo.top;
			},
			showOperate() {
				uni.showActionSheet({
					itemList:['登录','注册'],
					itemColor:'#433f9e',
					success: (e) => {
						if(e.tapIndex==1) {
							this.operate = '注册'
							this.accountPlaceholder="请输入邮箱";
							this.pswPlaceholder = "请输入密码";
							this.title2 = '填写注册信息'
						} else {
							this.operate = '登录'
							this.accountPlaceholder="请输入邮箱"
							this.pswPlaceholder="请输入密码"
							this.title2 = '验证身份信息'
						}
					}
				})
			},
			next() {
				if(this.operate == '注册') this.registerFlag = true
				else this.registerFlag = false
				this.currentIndex++;
			},
			turnBack(){
				if(this.currentIndex==2) uni.navigateBack()
				this.registerFlag = false
				this.currentIndex--;
			},
			navBack(position){
				if(position==0)  //从"暂不登录"和"返回"点击
					uni.navigateBack({})
				//登陆成功后点击整个页面返回
        else if(this.currentIndex==2){
          uni.navigateBack()
          setTimeout(() => {
            this.$common.calPercentage()
          }, 500);
        }
			},
			watchPwd(){
				this.showPwd = !this.showPwd;
			},
			codeChange(text) {
				this.tips = text;
			},
			getCode() {  //获取验证码
				if(!uni.$u.test.email(this.form.account)) uni.$u.toast('邮箱格式不正确');
				if(this.$refs.uCode.canGetCode) {
					uni.showLoading({title: '正在获取验证码'})
					userRegister(this.form.account).then(rs => {
						uni.hideLoading();
						uni.$u.toast('验证码已发送');
						this.$refs.uCode.start();
					},err=>{
						uni.hideLoading();
						uni.$u.toast(err.data.message);
					})
				} else {
					uni.$u.toast('倒计时结束后再发送');
				}
			},
			yimuLogin() { //一目登录
				const that = this;
				var account = that.form.account,psw = that.form.psw
				uni.request({url: `https://v.powerv.top/api/user/login?account=${account}&password=${psw}`}).then(rs => {
					let res = rs[1].data
					console.log('一目登录接口:',res);
					if(res.code == 401 || res.code == 205 ) uni.showToast({title: '帐号或密码有误',icon:'none'});
					else if(res.code == 101 || res.code == 201) uni.showToast({title: '😵智慧交大连接出错',icon:'none'});
					else
						if(res.code == 200) that.eventLogin() //老用户登录
						else if(res.code == 210) { //新用户注册
							uni.showLoading({title:'正在初始化新用户信息'})
							getUserEdu(account).then(async info => {
								//设置一下默认头像和昵称
								await updateUser({ userid:info.userid,avatar:'https://tva2.sinaimg.cn/large/0061jZbely8gteby2ezyoj30b40b4mxh.jpg',openname:'一问用户'})
								that.getInfoAndStore(info)
							})
						}
				})
			},
			eventLogin() {  //一问登录
				const that = this;
				if(uni.$u.test.email(that.form.account))
					userLogin({email:that.form.account,eduPassword:that.form.psw}).then(info => { that.getInfoAndStore(info) },err=>{
						uni.hideLoading();
						uni.$u.toast(err.data.message)
					})
				else 
					userLogin({eduAccount:that.form.account,eduPassword:that.form.psw}).then(info => { that.getInfoAndStore(info) },err=>{
						uni.hideLoading();
						uni.$u.toast(err.data.message)
					})
			},
			getInfoAndStore(info) { //用户登录或注册后获取用户信息放入缓存以及一系列操作
				uni.clearStorage();
				getUserIntro(info.userid).then(async intro => {
          if(!info.avatar) await updateUser({ userid:info.userid,avatar:'https://tva2.sinaimg.cn/large/0061jZbely8gteby2ezyoj30b40b4mxh.jpg',openname:'一问用户'})
          if(info.isNewUser) await updateUser({ userid:info.userid,avatar:info.avatar,openname:info.openname})
          let uinfo = {...info,...intro}
          createToken(info.userid).then(token => {uni.setStorageSync('token',token)}) //获取token
          uni.setStorageSync('userInfo', uinfo)     //合并信息后缓存
          if(this.wxUserInfo) uni.setStorageSync('wxUserInfo',this.wxUserInfo)
          uni.setStorageSync('hasLogin',true)
          uni.setStorageSync('userId',info.userid)
          uni.$emit("freshLocation",null)//更新附近人才
          this.$store.commit("setLoginStatus",true) //更新vuex状态
          this.$store.commit("setUserId",info.userid)
          this.$store.commit("setFresh", true)
          this.socketInit(info.userid)  //初始化即时通讯
          uni.hideLoading()
          if(this.registerFlag) uni.showToast({ title:'注册成功', icon:'success' });
          else uni.showToast({ title:'登陆成功', icon:'success' });
          if(this.isWxLogin){
            uni.setStorageSync('isWxLogin',true)
            this.currentIndex=this.currentIndex+2;
          }else this.currentIndex++; //跳到下一页
          this.prTop=588
        })
			},
			socketInit(userid) { //websocket初始化
				//即时通讯连接服务器
				uni.connectSocket({url: 'wss://www.mygirlfriends.cn/websocket/' + userid}); 
				//websocket监听函数
				uni.onSocketOpen(res => {console.log('WebSocket连接已打开！')});
				uni.onSocketError(res => {uni.showToast({title:'WebSocket连接失败!',icon:'none'}) });
				uni.onSocketClose(res => {console.log('WebSocket 已关闭！') });
				uni.onSocketMessage(res => {
					var getMsg = JSON.parse(res.data)
					var tempMsg = uni.getStorageSync('tempMsg');let temp = new Map(tempMsg) //由Array转为Map
					var unreadNum = uni.getStorageSync('unreadNum');
					if (getMsg instanceof Array) { //未读消息
						let msgList = uni.getStorageSync('msgList')
						let len = getMsg.length
						if (len > 0) {
							uni.setStorageSync('tempMsg',getMsg)
							uni.setTabBarBadge({index: 3,text: String(unreadNum+len)})
							uni.setStorageSync('unreadNum',unreadNum+len);
							uni.showToast({title: '你有' + len + '条新的未读消息',icon: 'none'})
						} else {
							if(unreadNum) uni.setTabBarBadge({index: 3,text: String(unreadNum)})
							if(!msgList) {//第一次连接
								uni.setStorageSync('msgList',[]) 
								uni.setStorageSync('tempMsg',[])
							}
						}
					} else {  //实时消息
						//在打开消息页面之前，实时消息都只能先存在缓存tempMsg中,打开消息页面后再与msgList合并
						if(temp.has(getMsg.chatSenderId)) {
							let x = temp.get(getMsg.chatSenderId)
							getMsg.badge = x.badge ? x.badge+1 : 1
						} else getMsg.badge = 1
						temp.set(getMsg.chatSenderId,getMsg);
						uni.showToast({title:'您收到一条消息',icon:'none'})
						uni.setTabBarBadge({index: 3,text: String(unreadNum+1)})
						uni.setStorageSync('tempMsg',[...temp])  //由Map转回Array
						uni.setStorageSync('unreadNum',unreadNum+1);
					}
				})
			},
			// 提交表单
			submit() {
				uni.showLoading({title:'请稍后'})
				if(!this.registerFlag) { //如果是登录的话
					if(uni.$u.test.email(this.form.account)) 
						this.eventLogin() 			//帐号为邮箱走一问接口
					else this.yimuLogin()			//帐号为学号走一目接口
				} else {  //如果是注册的话
					if(!uni.$u.test.email(this.form.account)) uni.$u.toast('邮箱格式不正确');
					validRegister(this.form.account,this.form.psw,this.form.code).then(rs => {this.getInfoAndStore(rs)})
				}
			},
			navToPrivacy(){
				uni.navigateTo({url:"/subPackageA/pages/me/setting/me-privacy"})
			},
      login(){
        this.isWxLogin=true;
        if(this.loginRes.isNewUser){
          uni.getUserProfile({
          desc:'请求获取用户名与头像',
          success:res=>{
            console.log(res)
            var wxInfo=res.userInfo;
            var info=this.loginRes;
            this.wxUserInfo=res.userInfo;
            info.openname=wxInfo.nickName;
            info.avatar=wxInfo.avatarUrl;
            this.getInfoAndStore(info);
            bindWxInfo(this.loginRes.userid).then(res=>{console.log(res)},err=>{console.log(err)})
          },
          fail:err=>{
            console.log(err)
            this.getInfoAndStore(this.loginRes);
            bindWxInfo(this.loginRes.userid).then(res=>{console.log(res)},err=>{console.log(err)})
          }
          })
        }else{
          this.getInfoAndStore(this.loginRes);
        }
        
			  }
		}
	}
</script>

<style lang="scss">
	@keyframes fadein{
		0%{
			opacity: 0;
		}
		100%{
			opacity: 1;
		}
	}
	page{
		background-color: #f8f8f8;
	}
	.topNav{
		position: fixed;
		z-index: 2;
		width: 100%;
		background-color: #94bbff;
	}
	.topNav-content{
		position: relative;
		overflow: hidden;
		height: 45rpx;
		width: 100%;
	}
	.topNav-words{
		position: absolute;
		color: #FFFFFF;
		font-size: 40rpx;
		font-weight: 700;
		left: 275rpx;
		bottom: 10rpx;
	}
	.cancel-image{
		animation:fadein .5s ease-in 1;
		top: 15rpx;
		position:relative;
		left: 30rpx;
		width: 30rpx;
	}
	.container{
		background-color: #94bbff;
	}
	.top-container{
		height: 30%;
	}
	.top-image{
		animation:fadein 1s ease-in 1;
		position: relative;
		width: 600rpx;
		margin: 50rpx 0 50rpx 75rpx;
	}
	.info-container{
		width: 750rpx;
		height: 55%;
		border-radius:40rpx 40rpx 0 0;
		background-color: #f8f8f8;
		overflow: hidden;
	}
	.top-description{
		animation:fadein .5s linear 1;
		color: #6c63ff;
		font-size: 50rpx;
		font-weight: 700;
		position: relative;
		text-align: center;
		margin: 80rpx 0 0 0;
	}
	
	.stack1-middle-wrap{
		background-color: #FFFFFF;
		width: 650rpx;
		overflow: hidden;
		height: 100rpx;
		border-radius: 30rpx;
		margin: 100rpx auto 0 auto;
		position: relative;
	}
	.stack1-middle-words{
		font-size: 30rpx;
		line-height: 100rpx;
		color: #4f4dc8;
		display: flex;
		justify-content: center
	}
	.stack1-middle-icon{
		position: absolute;
		right: 30rpx;
		top: 35rpx;
		/* #ifdef MP-QQ */
		top: 18rpx;
		/* #endif */
	}
	.tips {
		float: right;
		margin-right: 57rpx;
		margin-top: 8rpx;
		text {
			color: #9b9b9b;
			font-size: 20rpx;
		}
	}
	.bottom-btn{
		animation:fadein .5s linear 1;
		height: 110rpx;
		width: 650rpx;
		line-height: 110rpx;
		border-radius: 30rpx;
		background-color: #6c63ff;
		font-size: 35rpx;
		font-weight: 600;
		position: relative;
		margin: 50rpx auto 0 auto;
		text-align: center;
		color: #FFFFFF;
		overflow: hidden;
	}
	.wxLogin{
		height: 100rpx;
		width: 650rpx;
		margin: 50rpx auto 0 auto;
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 30rpx;
		background-color: #FFFFFF;
		.wxWord{
			font-size: 30rpx;
			color: #4f4dc8;
		}
	}
	.cancel{
		color: #888888;
		position: absolute;
		bottom: 100rpx;
		left: 50rpx;
	}
	.stack2-middle-wrap{
		position: relative;
		margin: 30rpx 0 0 0;
	}
	.stack2-middle-item{
		height: 100rpx;
		width: 650rpx;
		margin: 50rpx auto 0 auto;
		border-radius: 30rpx;
		background-color: #FFFFFF;
		position: relative;
	}
	.stack2-middle-item-icon{
		position: absolute;
		left: 50rpx;
		top: 30rpx;
	}
	.stack2-middle-item-input{
		position: absolute;
		width: 500rpx;
		left: 120rpx;
		top: 30rpx;
	}
	.stack2-middle-showPwdIcon{
		position: absolute;
		z-index: 2;
		height: 50rpx;
		width: 50rpx;
		right: 20rpx;
		top: 25rpx;
	}
	.code-btn {
		width: 162rpx;
		height: 60rpx;
		border-radius: 13rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		position: absolute;
		right: 4px;
		top: 22rpx;
		font-size: 24rpx;
		background-color: #6F60FC;
		color: #fff;
	}
	.privacy-wrap{
		position: relative;
		text-align: center;
		color: #9b9b9b;
		margin: 50rpx 0 0 0;
	}
	.privacy-nav{
		color: #6c63ff;
	}
	.stack3-wrap{
		position: relative;
		top: 200rpx;
	}
	.stack3-item{
		color: #6c63ff;
		font-size: 50rpx;
		text-align: center;
		position: relative;
		font-weight: 700;
	}
</style>