<template>
	<view class="content">
		<u-modal :show="showIt" negativeTop=200 :title="title" showCancelButton=true @confirm="confirm()" confirmColor="#7688FC" @cancel="cancle()">
			<view class="pop">
				<view class="input">
					<input :placeholder="placeholder" border="surround" :value="value" @input="input($event.detail.value)" />
				</view>
			</view>
		</u-modal>
	</view>
</template>

<script>
	export default {
		name: 'modalForQQ',
		props: {
			value: {
				type: String,
				default: ''
			},
			show: {
				type: Boolean,
				default: false
			},
			title: {
				type: String,
				default: '',
			},
			placeholder: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				showIt:this.show
			};
		},
		watch: {
			show(val) {
				this.showIt = val
			}
		},
		methods: {
			confirm() {
				this.$emit('confirm')
			},
			cancle() {
				this.$emit('cancle')
			},
			input(val) {
				this.$emit('input', val);
			}
		}
	}
</script>

<style lang="scss">
	.content {
		position: fixed;
		z-index: 99;
		top: 35%;
		left: 45%;
		background-color: #007AFF;
	
		.pop {
			width: 550rpx;
			padding: 32rpx 20rpx;
			display: flex;
	
			.title {
				width: 100%;
				height: 100%;
				display: flex;
				align-items: center;
				justify-content: center;
			}
	
			.input {
				width: 100%;
				height: 87rpx;
				border: #d4d4d4 1px solid;
				margin-right: 19rpx;
				border-radius: 11rpx;
				padding-top: 18rpx;
				padding-left: 10rpx;
			}
		}
	}
</style>

