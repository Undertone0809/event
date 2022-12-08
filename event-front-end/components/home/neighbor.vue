<template>
	<view class="all">
		<scroll-view 
      scroll-y 
      class="all" 
      refresher-enabled 
      enable-back-to-top 
      lower-threshold=250 
      @scrolltolower="reachBottom()" 
      :refresher-triggered="refreshStatus" 
      @refresherrefresh="pullDownRefresh()"
    >
			<view class="flex-col">
				<view v-if="show">
					<view v-if="teams.length==1" class="no-one">
						<image class="img" src="https://s2.loli.net/2022/02/19/nqME1VH283L5zyb.png"></image>
						<view class="des">
							附近人才似乎都去旅游了..
						</view>
					</view>
					<view v-else class="content">
						<!-- <view @click="goUser(item)" class="item" v-for="(item,index) in users" :key="index">
							<view class="left">
								<image :src="item.avatar" class="img"></image>
								<view class="des">
									<view class="name">{{item.openname}}</view>
									<view class="email">{{item.email || '@none'}}</view>
								</view>
							</view>
							<view class="right">
								距离{{distance[index+1].distance.value.toFixed(1)}}km
							</view>
						</view> -->
						<view class="userBar">
							<scroll-view scroll-x="true" @scrolltolower="moreUser()" lower-threshold="50rpx">
								<view class="userBar">
									<view @click="goUser(item)" class="item" v-for="(item,index) in users" :key="index">
										<image :src="item.avatar" class="img"></image>
										<view class="name">
											{{item.openname}}
										</view>
									</view>
								</view>
							</scroll-view>
						</view>
						<view class="content" v-for="(item,index) in teams" :key="index">
						  <team-item :teamData="item" :hasDistance='true'></team-item>
						</view>
					</view>
				</view>

				<view v-else>
					<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
						<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
							title loading avatar></u-skeleton>
					</view>
				</view>
				
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import { getNeighbor,getNearTeam } from '@/common/API.js'
  import { FRESH_NEIGHBOR_COMPONENT } from 'common/topic_enum.js'
  import teamItem from "@/components/home/team-item.vue"
	export default {
		components: { teamItem },
		data() {
			return {
        show: false,
				teams: [],
				users: [],
				distance: [],
        refreshStatus: false,
				noMore: false,
        pageNum: 1,
        longitude: 0,
        latitude: 0,
        
			}
		},
		mounted() {
      this.$disp.on(FRESH_NEIGHBOR_COMPONENT,()=>{ 
        this.freshData()
        // 如果骨架屏加载过久，请提示用户判断是否打开定位
        setTimeout(()=>{
          if(!this.show) uni.showToast({ title: '请检查是否授权开启定位功能',icon: 'none',duration: 2000 })
        },3000)
      })
      
      this.init()
		},
		methods: {
      init(){
        uni.$on("freshLocation",res=>{
          
            uni.authorize({
              scope: 'scope.userLocation',
              
              success:()=> { this.getLocation() },
              
              fail:()=> {
                uni.showModal({
                  title: '未打开小程序定位',
                  content: '找不到您的位置，请开启定位',
                  confirmText: '开启定位',
                  showCancel: false,
                  success: (res) => {
                    if (res.confirm)
                      wx.openSetting({ success: (res) => {console.log("[info] open location successfully");this.refresh=true} }); // 打开地图权限设置
                  }
                })
              }

            })
          
        })
        if(!uni.getStorageSync("hasLogin")){
          this.show=false;
          //uni.navigateTo({url: '/subPackageA/pages/me/login'});
        }else{
          uni.authorize({
            scope: 'scope.userLocation',
            success:()=> { 
              this.getLocation() 
            },
            fail:()=> {
              uni.showModal({
                title: '未打开小程序定位',
                content: '找不到您的位置，请开启定位',
                confirmText: '开启定位',
                showCancel: false,
                // 打开地图权限设置 
                success: (res) => { if (res.confirm) wx.openSetting({ success: (res) => {console.log("[info] open location successfully");this.refreshStatus=true} });}
              })
            }
          })
        }
      },
      freshData(){
        this.pageNum = 1
        this.teams = []
        this.getNearTeam()
      },
      getNearTeam(){
        getNearTeam(uni.getStorageSync('userId'),this.longitude,this.latitude,this.pageNum,7).then(res => {
          if(res.teamInfo.length==0){
            this.noMore=true
            uni.showToast({ title: '没有更多了',icon: 'none',duration: 2000 })
          }else{
			  console.log(res)
            this.teams = this.teams.concat(res.teamInfo)
            this.distance = res.distance
            this.pageNum++
            this.refreshStatus = false
			      this.show = true
            this.teams.forEach(team=>{
              this.distance.forEach(item => { 
                if(team.teamLeader == item.content.name) 
                  team.distance = item.distance.value == '0'? '<1':item.distance.value  
              })
            })
            
          } 
        },err=>{ console.warn(`[error] getNearTeam: ${JSON.stringify(err)}`) })
      },
			reachBottom() {  //触底触发
				this.getNearTeam()
				if(this.noMore) return;
			},
			moreUser(){
				getNeighbor(uni.getStorageSync('userId'),this.longitude,this.latitude).then(rs => {
          this.users = rs.userInfo.concat(rs.userInfo)
          // 去除users中的null数据
          this.users = this.users.filter(item => item != null)
          // this.distance = rs.distance
				},err=>{ console.warn(`[error] go error: ${JSON.stringify(err)}`) })
			},
			async pullDownRefresh() { //下拉刷新
			this.refreshStatus = true
			this.pageNum = 0
			this.teams = []
			this.show = false
			this.getNearTeam()
			},
			getLocation(){
				uni.getLocation({
					type: 'wgs84',
					success: (res) => {
            console.log(`[info] get location successfully, latitude: ${res.latitude}, longitude: ${res.longitude}`)
            this.longitude = res.longitude
            this.latitude = res.latitude

            this.freshData()
            getNeighbor(uni.getStorageSync('userId'),res.longitude,res.latitude).then(rs => {
              this.users = rs.userInfo
              this.users = this.users.filter(item => item != null)
            },err=>{ console.warn(`[error] go error: ${JSON.stringify(err)}`) })
            
						// this.$nextTick(()=>{ this.show = true })
					}
				})
			},
      goUser(item) {
				uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+item.userid})
			},
			 
		}
	}
</script>

<style lang="scss" scoped>
	.all {
		height: 100%;
		flex: 1;
	}
	.no-one {
		width: 100vw;
		height: 84vh;
		background-color: #EFF2F3;
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.img {
			width: 100%;
			height: 57vh;
		}
		
		.des {
			color: #302C48;
			font-weight: bold;
			font-size: 36rpx;
		}
	}
	.content {
		width: 100%;
		height: 100%;
		margin-bottom: 30rpx;
		.userBar{
      width: 100%;
      height: 200rpx;
      display: flex;
      justify-content: space-between;		
      align-items:center;	
      white-space: nowrap;
      border-bottom: 2px solid linear-gradient(270deg, #24fbe7 0%, #669cff 100%);
      padding-left: 12rpx;
      .item{
        padding:10rpx 15rpx;
        display:flex;
        flex-direction: column;
        align-items: center;
        
        .img{
          width: 95rpx;
          height: 95rpx;
          border-radius: 50%;
          border: 1px solid #ececec;
        }
        .name{
          width: 125rpx;
          margin-top: 10rpx;
          font-size: 25rpx;
          color: #484848;
          text-align: center;
          word-break: break-all;
          text-overflow: ellipsis;
          overflow: hidden;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2; /* 这里是超出几行省略 */
        }
      }
    }
		// .item {
		// 	width: 100%;
		// 	height: 133rpx;
		// 	padding: 15rpx 20rpx;
		// 	display: flex;
			
		// 	.left {
		// 		display: inline-flex;
				
		// 		.img {
		// 			width: 110rpx;
		// 			height: 110rpx;
		// 			border-radius: 50%;
		// 			border: 1px solid #ececec;
		// 		}
		// 		.des {
		// 			width: 469rpx;
		// 			height: 110rpx;
		// 			padding-left: 30rpx;
		// 			display: flex;
		// 			flex-direction: column;
		// 			justify-content: center;
					
		// 			.name {
		// 				font-size: 32rpx;
		// 				color: #484848;
		// 			}
					
		// 			.email {
		// 				font-size: 22rpx;
		// 				color: #848484;
		// 				margin-top: 12rpx;
		// 			}
		// 		}
		// 	}
			
		// 	.right {
		// 		width: 200rpx;
		// 		height: 110rpx;
		// 		line-height: 110rpx;
		// 		color: #9e9e9e;
		// 		font-size: 24rpx;
		// 	}
			
			
		// }
	}
</style>
