## Event

**一问Event**是一个面向大学生的赛事组队交流分享平台微信小程序，采用前后端分离的方式构建，前端采用uniapp跨平台编译到微信小程序，后端采用springcloud nacos+redis+rabbitmq+mysql的分布式技术栈构建，该项目当前因为服务器到期暂时不提供体验版本，如果想要体验可以尝试在本地构建项目，或者查看演示视频

[![一问Event-大学生赛事交流平台](https://res.cloudinary.com/marcomontalbano/image/upload/v1714028818/video_to_markdown/images/youtube--u4nbmzymK8s-c05b58ac6eb4c4700831b2b3070cd403.jpg)](https://www.youtube.com/watch?v=u4nbmzymK8s "一问Event-大学生赛事交流平台")

一问Event——一个面向大学生的赛事组队交流分享平台。分布在不同大学、不同专业领域的大学生可以为你提供独特而又价值的一手赛事信息，也可以找到和你志同道合的人一同参与比赛，无论你处于大学生活的哪个阶段，都可以来Event听听大家的现身说法。

【赛事想法分享】有内容有观点，你想看赛事交流分享都在这里

【组队个性化推荐】根据用户名片个性化推荐组队内容，寻找潜在的专业人才。

【打造专业形象】在个人主页上加上“赛事经历”、“技术栈”等背书，让你的独特价值获得更多的机遇垂青。

【扩展人脉神器】各领域海量专业人才，更加方便地扩展专业领域人脉。

【跨领域合作有门路】找人搜索关键词，快速精准地找到相关领域专业人才。

## 安装

### 后端
> 关于后端环境，你需要先安装好mysql,rabbitmq,nacos的环境，当然本项目为分布式项目，如果仅仅想要运行部分模块的话不一定用得上rabbitmq。

1. 导入sql文件到你的数据库中，sql文件在`.\event-back-end\sql`中
2. 去nacos官网下载并成功运行一个nacos项目
3. 你需要先配置好每个模块中`application-dev.yml`和`application-prod.yml`中的配置信息，如果当前你只需要在本地开发部署则只需要配置`application-dev.yml`即可，需要配置的信息包括`数据库信息、nacos、rabbitmq、mail、redis、wxid`。
4. 配置好信息之后先编译`Event_common`模块，然后就可以依次运行`Event-chat`，`Event-comment`，`Event-post`,`Event-user`模块。
### 前端
1. 安装HBuliderX和微信小程序开发者工具
2. 在HBuliderX中打开该项目
3. 在HBuliderX中安装scss、uview-ui库
4. 修改`manifest.json`中的微信小程序appid，修改为自己的appid

<img src="https://zeeland-bucket.oss-cn-beijing.aliyuncs.com/typora_img/20221208124550.png"/>


5. 运行到微信小程序
   
<img src="https://zeeland-bucket.oss-cn-beijing.aliyuncs.com/typora_img/20221208124203.png"/>

## TODO
该项目的后端现在是spring cloud分布式的，本人打算把它改成单体的项目开源出来，奈何精力有限，想找人一起把它重构开源出来，有意向的可以email我: zeeland@foxmail.com

## 如何成为本项目的贡献者

个人力量终究是渺小的，欢迎大家一起参与到本项目中，如果你在使用时发现了如下类型的问题，请提出issue进行反馈。

欢迎对本项目提出功能特性方面的建议，我们一起讨论，如果有可能我们会尽快实现，欢迎PRs。
