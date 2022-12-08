export default {
	commonData:{
		phoneData:{
			//手机数据
			statusBarHeight: 20,/* 状态栏高度 */
			navBarHeight: 45,/* 导航栏高度 */
			windowWidth: 375,/* 窗口宽度 */
			val: '',/* 导航栏搜索框的值 */
			menuButtonWidth: 20,//胶囊宽度
			menuButtonHeight:20,//胶囊高度
			menuButtonInfo:{},
			phoneInfo:{//手机信息(单位rpx)
				height:10,
			},
			/*手机的rpx比例,为750/手机宽度的比例,其中px*phoneScale为rpx的长度*/
			phoneScale:2,
			
		},
		//全局css
		styleSheet:{
			mainColor:"#d64233",
			gradientMainColor: "linear-gradient(to right, #d64233, 44%, #d64255)",
		}
	},
	// 初始化数据，用于获取手机数据
	initData(){
		// 获取手机系统信息
		const info = uni.getSystemInfoSync();
		// 设置状态栏高度（H5顶部无状态栏小程序有状态栏需要撑起高度）
		this.commonData.phoneData.statusBarHeight = info.statusBarHeight;
		this.commonData.phoneData.phoneScale = 750/ this.commonData.phoneData.windowWidth;//rpx的比例
		//计算手机rpx高度
		this.commonData.phoneData.phoneInfo.height = info.windowHeight * this.commonData.phoneData.phoneScale;
		
		// 除了h5 app mp-alipay的情况下执行
		// #ifndef H5 || APP-PLUS || MP-ALIPAY
		// 获取胶囊的位置
		this.commonData.phoneData.menuButtonInfo = uni.getMenuButtonBoundingClientRect();
		// (胶囊底部高度 - 状态栏的高度) + (胶囊顶部高度 - 状态栏内的高度) = 导航栏的高度
		this.commonData.phoneData.navBarHeight = (this.commonData.phoneData.menuButtonInfo.bottom - info.statusBarHeight) + (this.commonData.phoneData.menuButtonInfo.top - info.statusBarHeight);
		//获取胶囊最左边的位置到屏幕右边所占的长度
		//获取胶囊的高度
		this.commonData.phoneData.menuButtonWidth = (this.commonData.phoneData.windowWidth - this.commonData.phoneData.menuButtonInfo.left) * this.commonData.phoneData.phoneScale;
		this.commonData.phoneData.menuButtonHeight = (this.commonData.phoneData.menuButtonInfo.bottom - this.commonData.phoneData.menuButtonInfo.top) * this.commonData.phoneData.phoneScale ;
		// #endif
	},
  /**
   * @description 检查用户个人信息完成度
   * @param {*} need2Show 是否需要弹出提示框
   * @returns {[percentage,hasLogin]} percentage 用户信息完成度，hasLogin 是否已经登录
   */
  calPercentage(need2Show = true){
    let percentage = 0
    let userInfo = uni.getStorageSync('userInfo')
    if(uni.$u.test.isEmpty(userInfo)) {
      // 未登录
      return [0,false]  
    }
    if(userInfo.userResume || userInfo.userResume != ''){
      percentage = percentage + 20
    }
    if(userInfo.userEdu || userInfo.userEdu != ''){
      percentage = percentage + 20
    }
    if(userInfo.userExpr || userInfo.userExpr != ''){
      percentage = percentage + 20
    }
    if(userInfo.userSkill || userInfo.userSkill != ''){
      percentage = percentage + 20
    }
    if(userInfo.userPrize || userInfo.userPrize != ''){
      percentage = percentage + 20
    }
    if(need2Show && percentage < 60){
      uni.showModal({
        title: '提示',
        content: '当前资料完成度过低，请完善个人名片，让你的独特价值获得更多的机遇垂青！',
        showCancel: false,
        confirmColor: '#f56c6c',
        confirmText: '去完善',
        success:function(res){
          if(res.confirm) uni.navigateTo({ url: '/subPackageA/pages/me/me-edit' })
        }
      })
    }
    return [percentage,true]
  },
}