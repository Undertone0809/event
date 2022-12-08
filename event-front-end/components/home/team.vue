<template>
	<view class="all">
		<scroll-view scroll-y class="all"  :refresher-enabled="fromPage=='contestDetail'?false:true" enable-back-to-top lower-threshold=250 @scrolltolower="reachBottom()" :refresher-triggered="refresh" @refresherrefresh="pullDownRefresh()">
			<view class="flex-col">
				
				<view v-if="show">
					<view v-if="teamList.length!=0">
						<view class="content" v-for="item in teamList" :key="item.teamId">
							<team-item :teamData="item"></team-item>
						</view>
						<view class="bottomBar">
							---没有了---
						</view>
					</view>
					<view v-else>
						<u-empty
						        mode="search"
						        icon="http://cdn.uviewui.com/uview/empty/search.png"
						>
						</u-empty>
					</view>
				</view>

				<view v-else>
					<view style="margin:40rpx 0 0 30rpx" v-for="(item,index) in 2" :key='index'>
						<u-skeleton :rows="9" :rowsWidth="['40%', '88%', '88%' ,'35%','35%','35%','88%','88%','40%']"
							title loading avatar></u-skeleton>
					</view>
				</view>
				
			</view>

      <blank-placeholder/>
		</scroll-view>
	</view>
</template>

<script>
  import blankPlaceholder from 'components/common/blank-placeholder'
	import {getNotFinishedTeam,getTeamBySearch,getTeamByContest} from '@/common/API.js'
	import teamItem from "@/components/home/team-item.vue"
  import { FRESH_TEAM_COMPONENT } from 'common/topic_enum.js'
	export default {
		components: {
			teamItem,
			blankPlaceholder
		},
		props: {
		  keyWord:{
		  	type: String,
		  	default(){ return '' }
		  },
		  fromPage:{
		  	type: String,
		  	default(){ return '' }
		  },
		},
		data() {
			return {
				refresh: false,
				teamList: [],
				show: false,
				page: 0,
				noMore: false
			}
		},
    mounted() {
      this.initData()  
      this.$disp.on(FRESH_TEAM_COMPONENT,()=>{ this.initData() })

    },
		methods: {
      initData(){
        this.page = 0
        // 判断从哪个页面来的
        if(this.fromPage=='searchDetail'){
          const p={
            "keyword": this.keyWord,
            "pageNum": this.page+1,
            "pageSize": 5
          }
          getTeamBySearch(p).then(rs=>{
            this.teamList = rs
            this.show = true
            this.refresh = false
          },err=>{
            console.log(err)
          })
        }else if(this.fromPage=='contestDetail'){
          getTeamByContest(this.keyWord,this.page+1,5).then(rs=>{
            this.teamList = rs
            this.show = true
            this.refresh = false
          },err=>{
            console.log(err)
          })
        }else{
          getNotFinishedTeam(5,this.page).then(rs => {
            this.teamList = rs
            this.show = true
            this.refresh = false
          },err=>{
            console.log(err)
          })
        }
       
      },
      getMoreData(){
        this.page++;
		// if(this.keyWord!=''){
		// 	const p={
		// 		"keyword": this.keyWord,
		// 		"pageNum": this.page+1,
		// 		"pageSize": 5
		// 	}
		// 	getTeamBySearch(p).then(rs=>{
		// 		if (rs.length) this.teamList = this.teamList.concat(rs)
		// 		else this.noMore = true
		// 	})
		// }else{
		// 	getNotFinishedTeam(5,this.page).then(rs => {
		// 		console.log(rs)
		// 		if (rs.length) this.teamList = this.teamList.concat(rs)
		// 		else this.noMore = true
		// 	})
		// }
		if(this.fromPage=='searchDetail'){
			const p={
				"keyword": this.keyWord,
				"pageNum": this.page+1,
				"pageSize": 5
			}
			getTeamBySearch(p).then(rs=>{
				if (rs.length) this.teamList = this.teamList.concat(rs)
				else this.noMore = true
			})
		}else if(this.fromPage=='contestDetail'){
			getTeamByContest(this.keyWord,this.page+1,5).then(rs=>{
				if (rs.length) this.teamList = this.teamList.concat(rs)
				else this.noMore = true
			})
		}else{
			getNotFinishedTeam(5,this.page).then(rs => {
				console.log(rs)
				if (rs.length) this.teamList = this.teamList.concat(rs)
				else this.noMore = true
			})
		}
		console.log(this.teamList)
      },
			reachBottom() { //分页处理
				if(this.noMore) return;
				this.getMoreData();
				console.log(555)
			},
			async pullDownRefresh() {
				this.refresh = true
				this.show = false
				this.initData()
				this.$nextTick(() => { this.show = true })
			},
		},
	}
</script>

<style lang="scss" scoped>
	.all {
		height: 100%;
		flex: 1;
	}
	.content {
		width: 100%;
		height: 100%;
		margin: 30rpx 0;
	}
	.bottomBar {
		width: 100%;
		padding-bottom: 100rpx;
		padding-top: 80rpx;
		display: flex;
		justify-content: center;
		color: rgb(153, 153, 153);
	}
</style>
