<template>
	<div class="friend-list animate">
		<div class="friend-item" v-for="item in list" :key="item.id">
			<view @click="goUser(item)">
				<div class="left">
					<image
						:src="item.avatar"
						class="avatar" />
				</div>
				<div class="right">
					<div class="site-name">{{item.Name}}</div>
					<div class="site-detail">{{item.desc}}</div>
				</div>
			</view>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				list: []
			}
		},
		onLoad() {
			var list = [{
				id: 13,
				Name: 'Zeeland',
				desc: '287017217@qq.com',
				avatar: 'https://thirdwx.qlogo.cn/mmopen/vi_32/n8dN2bLOFalWx3xrrDo0EoyNcAVed3AGcoqRvsQA2YK4qbQBt8PTNjpKV0cIGMLHAymib2nI5dKwibZROliaB78LQ/132'
			}, {
				id: 23,
				Name: '孤雏',
				desc: '501664112@qq.com',
				avatar: 'https://q1.qlogo.cn/g?b=qq&nk=501664112&s=640'
			}, {
				id: 22,
				Name: '橘子',
				desc: '1806689762@qq.com',
				avatar: "https://blog-cxz.oss-cn-beijing.aliyuncs.com/event16448098868021023155.jpg"
			}, {
				id: 217,
				Name: 'Seaaa',
				desc: '319677245@qq.com',
				avatar: 'https://thirdwx.qlogo.cn/mmopen/vi_32/slicgYiapN3VXSobNZjGNibic7qlsrFJfNfJQQ7PWfNibAMw2tjuRYibVXBdgQoibVuUmnq0JDdR6N3YVc0TcLExrUfIg/132'
			}, {
				id: 32,
				Name: '书恒',
				desc: '514156757@qq.com',
				avatar: 'https://s1.328888.xyz/2022/05/15/qbQ0y.jpg'
			}]
			list.sort((x, y) => Math.random() > .5 ? -1 : 1);
			this.list = list
		},
		methods: {
			goUser(item) {
				uni.navigateTo({ url: '/subPackageA/pages/me/user?userId='+item.id})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.animate {
		animation: main 1.5s;
	}

	.friend-list {
		width: 100%;

		.friend-item {
			display: inline-block;
			width: 90%;
			/*height: 100px;*/
			margin: 0 5% 20px 0;
			padding: 10px 30px;
			border: 1px solid #ECECEC;
			border-radius: 3px;

			&:hover {
				box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
			}

			&:nth-of-type(3n) {
				margin-right: 0;
			}

			.left {
				display: inline-block;

				.avatar {
					width: 150rpx;
					height: 150rpx;
					margin-right: 40rpx;
					border-radius: 50%;
				}
			}

			.right {
				display: inline-block;

				.site-name,
				.site-detail {
					// white-space: nowrap;
					// text-overflow: ellipsis;
					overflow: hidden;
					padding-bottom: 10px;
					line-height: 1.5rem;
					color: #636363;
				}

				.site-name {
					border-bottom: 1px dotted #ECECEC;
					background-image: linear-gradient(-3deg, #ffffff 0%, #ff6464 100%);
					font-size: 41rpx;
					-webkit-background-clip: text;
					color: transparent;
					font-family: monospace;
				}

				.site-detail {
					font-size: 13px;
					padding-top: 10px;
				}
			}

		}
	}
</style>
