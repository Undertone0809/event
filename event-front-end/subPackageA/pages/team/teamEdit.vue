<template>
	<view v-if="show">
		<team-send :item="item"/>
	</view>
</template>

<script>
	import {getTeam} from '@/common/API.js'
	import teamSend from "./teamSend.vue"
	export default {
		components:{teamSend},
		data() {
			return {
				show:false,
				item:undefined
			};
		},
		onLoad(option) {
			getTeam({teamId:option.teamId}).then(rs=>{this.item = rs[0];this.show = true})
		}
		
	}
</script>

<style lang="scss">

</style>
