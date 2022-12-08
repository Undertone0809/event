<template>
<!-- 发布想法 -->
	<view class="create">
		<!-- <view class="titleCreate">
			<textarea class="titleArea"  :auto-height="true" v-model="title" placeholder="请输入标题" placeholder-style="color:#D9D9DA" :show-confirm-bar="false" />
		</view> -->
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
		<view class="contentCreate">
			<textarea class="contentArea" v-model="content" placeholder="说说你的想法吧..."
			 placeholder-style="color:#D9D9DA" :show-confirm-bar="false" maxlength="500"/>
			<u-upload :fileList="imgList" width="223rpx" height="223rpx" maxSize="2097152" :deletable="true"
			@afterRead="afterRead" @delete="deletePic" name="1" :multiple="true" :maxCount="10">
			</u-upload>
		</view>
		
		<view class="publish" @click="publish()">
			<image src="../../static/post/post.png" mode="aspectFill"></image>
		</view>
	</view>
</template>

<script>
	import {sendPost,uploadFile,getAllContest,addContestPost} from "@/common/API.js"
	export default {
		data() {
			return {
				title:"",//创建时的标题
				content:"",//创建时的内容
				item:{},
				imgList:[],
				contestList:[],
				close:true,
				onSelectTitle:'未选择比赛',
				onSelectId:''
				
			}
		},
		onLoad(option) {
			// 获取当前帖子长度得到新帖子的id
			this.item.id=option.length
			getAllContest().then(rs => {
				this.contestList = rs;
				console.log(rs)
			},err=>{
				console.log(err)
			})
		},
		methods: {
			// 格式化时间方法
			dateTime(date){
				let time =new Date(date);
				//获取time具体时间
				let d=time.getSeconds();
				let h=time.getHours();
				let m=time.getMinutes();
				let Y=time.getFullYear();
				let M=time.getMonth()+1;
				let D=time.getDate();
				return Y+'-'+M+'-'+D+' '+h+":"+m+":"+d;
			},
			// 发布帖子方法
			publish(){
				if(this.content=="")
				{
					uni.showToast({
						title:'请输入内容哦',
						icon:'none',
						duration:2000
					});
				}else{
					this.item.postTitle = "想法_"+new Date();
					console.log("想法_"+this.dateTime(new Date()))
					this.item.postContent=this.content;
					this.item.userId = uni.getStorageSync('userId');
					this.item.postType = 'bb';
					var urls = ''
					console.log(this.imgList)
					this.imgList.forEach((item,index)=>{
						urls = urls+item.url
						if(index!=this.imgList.length-1) urls+=' '
					});
					this.item.postImgUrl=urls;

          // 发布帖子
					sendPost(this.item).then((res) => {
            let postId = res.postId
	          // 添加想法对应的帖子
            if(this.onSelectId != ''){
              addContestPost(postId,this.onSelectId).then(res=>{
                uni.showToast({ title:'发布成功',duration:500 })
                uni.redirectTo({ url:'/subPackageA/pages/post/detail?postId='+postId+'&commentNum=0' })
              })
            }else{
              uni.showToast({ title:'发布成功',duration:500 })
              uni.redirectTo({ url:'/subPackageA/pages/post/detail?postId='+postId+'&commentNum=0' })
            }
              
          })		
				}
			},
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

<style  lang="scss" scoped>
	.create{
		display: flex;
		flex-direction: column;
		align-items: center;
		width: 100%;
	}
	.titleCreate{
		width: 100%;
		display: flex;
		justify-content: center;
		.titleArea{
			width: 95%;
			font-size: 40rpx;
			font-weight: bold;
			border-bottom: 8rpx solid #dd524d;
			padding-top: 60rpx;
			padding-bottom: 10rpx;
		}
	}
	.contentCreate{
		width: 95%;
		display: flex;
		flex-direction: column;
		.contentArea{
			width:  100%;
			height: 440rpx;
			font-size: 36rpx;
			padding-top: 60rpx;
			padding-bottom: 10rpx;
		}
	}
	.imageSelector{
		width: 300rpx;
		height: 300rpx;
		background-color: #f5f6fa;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-left: 20rpx;
		.imageIcon{
			width: 100rpx;
			height: 100rpx;
		}
	}
	.publish{
		width: 100rpx;
		height: 100rpx;
		background-color: #dd524d;
		position: fixed;
		bottom: 100rpx;
		right: 70rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 50rpx;
		image{
			width: 70rpx;
			height: 70rpx;
		}
	}
	
	.select{
		width: 95%;
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
		width: 95%;
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
</style>

