<template>
	<view class="content">
		<view class="topWrap">
		    <view class="topWrap_title">一问社区隐私保护政策</view>
		    <view class="topWrap_detail">版本：1.0 修订与生效时间：2021年9月1日</view>
		  </view>
		 <view class="midWrap">
		    <view class="midWrap_title">一、前言</view>
		    <view class="midWrap_detail">我们深知个人信息对您而言的重要性，也感谢您对我们的信任。南昌燃目网络科技有限公司（以下简称“一问社区”）尊重并保护您的隐私。我们将通过本政策向您说明一问社区会如何收集、存储、保护、使用及对外提供您的信息，并说明您享有的权利，其中要点如下：</view>
		    <view class="midWrap_detail">1、为了便于您了解您在使用我们的服务时，我们需要收集的信息类型与用途，我们将结合具体服务向您逐一说明。</view>
		    <view class="midWrap_detail">2、为了向您提供服务所需，我们会按照合法、正当、必要的原则收集您的信息。</view>
		    <view class="midWrap_detail">3、您可以通过本政策介绍的方式访问和管理您的信息、设置隐私功能、注销一问社区账户。</view>
		    <view class="midWrap_title">二、我们处理个人信息的法律依据</view>
		    <view class="midWrap_detail">如果您是中华人民共和国大陆地区的用户，我们将依据《中华人民共和国网络安全法》、《信息安全技术个人信息安全规范》（GB/T 35273-2017）以及其他相关法律法规收集和使用您的个人信息，为您提供一问社区产品和/或服务。 我们通常只会在征得您同意的情况下收集您的个人信息。 在某些情况下，我们可能还会基于法律义务或者履行合同之必需向您收集个人信息，或者可能需要个人信息来保护您的重要利益或其他人的利益。</view>
		    <view class="midWrap_title">三、我们如何收集和使用您的个人信息，以及哪些业务功能需要您的哪些个人信息</view>
		    <view class="midWrap_detail">我们会遵循正当、合法、必要的原则，出于本政策所述的以下目的，收集和使用您在使用服务过程中主动提供或因使用一问社区产品和/或服务而产生的个人信息。如果我们要将您的个人信息用于本政策未载明的其它用途，或基于特定目的将收集而来的信息用于其他目的，我们将以合理的方式向您告知，并在使用前再次征得您的同意。</view>
		    <view class="midWrap_subTitle">3.1 对您的在校身份进行认证</view>
		    <view class="midWrap_detail">您可自愿填写您可用于验证在校身份的信息，用于我们对您的身份进行认证，这是我们帮助您查询信息的基础，您也可以选择跳过认证，使用我们的其他服务。</view>
		    <view class="midWrap_subTitle">3.2 同步您的微信头像、昵称等个性化信息</view>
		    <view class="midWrap_detail">您可自愿授权同步您的微信头像、昵称等个性化信息，用于提升您的使用体验以及提高用户之间的辨识度。</view>
		    <view class="midWrap_subTitle">3.3 获取您参加的在校各类部门、社团的信息</view>
		    <view class="midWrap_detail">您可自愿填写参加的在校各类部门、社团的信息，用于为您提升相应集体的相关服务。</view>
		    <view class="midWrap_subTitle">3.4 征得授权同意的例外</view>
		    <view class="midWrap_subTitle">根据相关法律法规的规定，在以下情形中，我们可以在不征得您的授权同意的情况下收集、使用一些必要的个人信息：</view>
		    <view class="midWrap_detail">与国家安全、国防安全直接相关的；与公共安全、公共卫生、重大公共利益直接相关的；与犯罪侦查、起诉、审判和判决执行等直接相关的；出于维护您或其他个人的生命、财产等重大合法权益但又很难得到本人同意的；所收集的个人信息是您自行向社会公众公开的；从合法公开披露的信息中收集到您的个人信息，如从合法的新闻报道、政府信息公开等渠道；根据您的要求签订和履行合同所必需的；用于维护一问社区的产品和/或服务的安全稳定运行所必需的，例如发现、处置产品或服务的故障；法律法规规定的其他情形。</view>
		    <view class="midWrap_title">四、我们如何保护您的个人信息</view>
		    <view class="midWrap_detail">您的个人信息安全对于我们至关重要。我们将遵守相关法律法规，采取合理可行的措施，保护您的个人信息。防止信息遭到未经授权的访问、披露、使用、修改，避免信息损坏或丢失。</view>
		    <view class="midWrap_title">五、如何联系我们</view>
		    <view class="midWrap_detail">当您对本政策有任何疑问，可以发送邮件至 <text class="linkText" bindtap="getLink">287017217@qq.com</text> 咨询，我们将及时解决您的问题。一般情况下，我们将在15天内回复。</view>
		  </view>  
	</view>
</template>

<script>
	export default {
		data() {
		  return {}
		},
		methods: {

		}
	}
</script>

<style>
	.topWrap{
	  padding-top: 30rpx;
	}
	.topWrap_title{
	  text-align: center;
	  font-weight: 900;
	  font-size: 45rpx;
	  letter-spacing: 1rpx;
	}
	.topWrap_detail{
	  margin: 30rpx 0 0 30rpx;
	  font-size: 28rpx;
	  color: rgba(0,0,0,0.9);
	}
	.midWrap{
	  margin-top: 30rpx;
	  padding: 0 30rpx;
	  margin-bottom: 140rpx;
	}
	.midWrap_title{
	  margin-top: 30rpx;
	  font-weight: 800;
	  font-size: 32rpx;
	}
	.midWrap_detail{
	  margin-top: 30rpx;
	  font-size: 28rpx;
	  color: rgba(0,0,0,0.9);
	  text-indent: 60rpx;
	  letter-spacing: 1rpx;
	  line-height: 40rpx;
	}
	.midWrap_subTitle{
	  margin-top: 30rpx;
	  font-size: 28rpx;
	  font-weight: 700;
	  color: rgba(0,0,0,1.0);
	  text-indent: 60rpx;
	  letter-spacing: 1rpx;
	  line-height: 40rpx;
	}
	.linkText{
	  color: #2b90d9;
	  text-decoration: underline;
	}


</style>
