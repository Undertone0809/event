//https://www.uviewui.com/js/http.html
const http = uni.$u.http

//图片上传
export const uploadFile = (url) => http.upload('/oss/uploadfile',{params:{fileName:'event'},filePath:url,name: 'file'})

/*------------- 用户模块 ---------------*/	
//用户登录
export const userLogin = (p) => http.get('/user/login',{params:p})
//用户邮箱注册(调用改接口发送注册的验证码)
export const userRegister = (email) => http.post(`/user/toInsertUserByEmail/${email}`,{},{custom: {istoken: true}})
//验证注册验证码是否正确如果正确则进行账户注册
export const validRegister = (email,eduPsw,code) => http.post(`/user/insertUserByEmail/${email}/${eduPsw}/${code}`,{},{custom: {istoken: true}})
//发送邮箱验证码
export const sendEmailCode = (userid,email) => http.post(`/user/toUpdateEmail/${userid}/${email}`)
//验证邮箱验证码
export const validEmailCode = (userid,email,code) => http.post(`/user/updateEmail/${userid}/${email}/${code}`)
//微信登录
export const wxLogin = (code) => http.post(`/user/wxLogin/${code}`)
//绑定微信个人信息
export const bindWxInfo = (userId) => http.post(`/user/bindWxInfo/${userId}`)

//检验token
export const checkToken = (token) => http.post('/token/isToken/'+token,{},{custom: {istoken: true}})
//创建token
export const createToken = (userid) => http.post('/token/createToken/'+userid,{},{custom: {istoken:true}})

// 查询用户通过id
export const getUserById = (id) => http.get('/user/selectUserInfo/'+id)
//查询用户通过交大学号
export const getUserEdu = (id) => http.get('/user/selectUserByCode/'+id)
//查询用户简介通过id
export const getUserIntro = (id) => http.get('/intro/selectIntroById/'+id)
//更新用户表(切记一定要传userid, @params: openid,openname,avatar,email )
export const updateUser = (p) => http.post('/user/updateUser',p)
//更新用户简介表(切记一定要传userid, @params: userResume,userSkill,userEdu,userPrize,userExpr,userBgimg )
export const updateIntro = (p) => http.post('/intro/updateIntroById',p)

//查询附近人才
export const getNeighbor = (userid,longtitude,latitude) => http.post(`/user/nearPeople/${userid}/${longtitude}/${latitude}`)
//查询附近人才的组队
export const getNearTeam = (userid,longtitude,latitude,pageNum,pageSize) => http.post(`/user/nearTeam/${userid}/${longtitude}/${latitude}/${pageNum}/${pageSize}`)

//查询主动关注的人
export const getFollow = (userid) => http.get('/user/selectCollectionUser/'+userid)
//查询粉丝
export const getFans = (userid) => http.get('/user/beSelectCollectionUser/'+userid)
//查询主动关注的人数
export const getFollowNum = (userid) => http.get('/user/selectCollectionUserNum/'+userid)
//查询粉丝数
export const getFansNum = (userid) => http.get('/user/beSelectCollectionUserNum/'+userid)
//查询是否关注
export const hasFollowed = (aId,bId) => http.get(`/user/hasCollectionUser/${aId}/${bId}`,{custom: {needLogin: true}})
//关注
export const followHim = (myId,hisId) => http.post(`/user/collectionUser/${myId}/${hisId}`)
//取关
export const unfollowHim = (myId,hisId) => http.post(`/user/removeCollectionUser/${myId}/${hisId}`)
//通过关键字查询用户
export const getUserBySearch = (p) => http.post(`/user/searchUserByPage`,p)
//分页查询我关注的用户信息
export const getFollowedUser = (userId,pageNum,pageSize) => http.get(`/user/selectMyFollower/${userId}/${pageNum}/${pageSize}`)
//分页查询我的粉丝的用户信息
export const getFansUser = (userId,pageNum,pageSize) => http.get(`/user/selectMyFans/${userId}/${pageNum}/${pageSize}`)
//推荐用户
export const recommendUser = (userId,pageNum,pageSize) => http.get(`/user/recommendUser/${userId}/${pageNum}/${pageSize}`)

/*--------------- 聊天模块 ---------------*/
// 查询聊天
export const getMessage = (p) => http.get('/chat/selectChat',{params:p})
// 查询两个人的聊天记录
export const getChatTwo = (p,q,page) => http.get(`/chat/selectChatByTwo/${p}/${q}/20/${page}`)
// 发送消息
export const pushChat = (sender,receipter,msg) => http.post(`/chat/push/${sender}/${receipter}/${msg}`)

/*---------------- 组队模块 ---------------*/
//查询组队通用接口
export const getTeam = (p) => http.get('/team/selectTeam',{params:p})
//查询已加入的组队
export const getMyJoinTeam = (userid,page) => http.get(`/team/selectMyJoinTeam/${userid}/99/${page}`)
//查询未结束的所有组队（时间和人数均满足)
export const getNotFinishedTeam = (pageSize,page) => http.get(`/team/selectNotFinishedTeam/${pageSize}/${page}`)
//查询我关注的人发起的组队
export const getMyFriendTeam = (userid,page) => http.get(`/team/selectMyFriendTeam/${userid}/16/${page}`)
//添加新的队伍不用传发起时间和随机码
export const createTeam = (p) => http.post('/team/insertTeam',p)
//删除队伍
export const deleteTeam = (teamId) => http.post(`/team/deleteTeam/${teamId}`)
//用户加入队伍
export const joinTeam = (userId,teamId) => http.post(`/team/insertTeamJoin/${userId}/${teamId}`)
//查询加入某队伍的人
export const getJoinUser = (p) => http.get('/team/selectHasJoined',{params:p})
//查询是否加入队伍
export const hasJoinTeam = (userid,teamId) => http.get(`/team/hasJoinTeam/${userid}/${teamId}`,{custom: {needLogin: true}})
//组队信息更新接口
export const updateTeam = (p) => http.post('/team/updateTeam',p)
//用户退出队伍
export const quitTeam = async (userId,teamId) => {
	await http.get(`/team/selectHasJoined?teamId=${teamId}&userId=${userId}`).then(async rs => await http.post('/team/deleteTeamJoin/'+rs[0].joinId) )
}

//查询是否收藏组队
export const hasCollectTeam = (userid,teamId) => http.get(`/team/hasCollectionTeam/${userid}/${teamId}`,{custom: {needLogin: true}})
//查询是否点赞
export const hasLikeTeam = (userId,teamId) => http.get(`/team/hasLikeTeam/${userId}/${teamId}`,{custom: {needLogin: true}})
//收藏组队接口
export const collectTeam = (userId,teamId) => http.post(`/team/collectionTeam/${userId}/${teamId}`)
//点赞组队
export const likeTeam = (userId,teamId) => http.post(`/team/LikeTeam/${userId}/${teamId}`)
//查看组队收藏数接口
export const getCollectNum = (teamId) => http.get('/team/beSelectCollectionTeamNum/'+teamId)
//查看组队点赞数接口
export const getLikeNum = (teamId) => http.get('/team/beSelectLikeTeamNum/'+teamId)
//取消组队收藏接口
export const unCollectTeam = (userId,teamId) => http.post(`/team/removeCollectionTeam/${userId}/${teamId}`)
//取消组队点赞接口
export const disLikeTeam = (userId,teamId) => http.post(`/team/removeLikeTeam/${userId}/${teamId}`)
//查看用户的收藏接口
export const getCollectTeam = (userid) => http.get('/team/selectCollectionTeam/'+userid)
//根据关键字搜索组队
export const getTeamBySearch = (p) => http.post(`/team/searchTeam`,p)

/*---------------- 想法模块 ---------------*/
//Post （闲聊想法）
//通过用户id分页查询该用户的想法
export const getPostByUserId = (userId,page) => http.get(`/post/selectPostByUserId/${userId}/99/${page}`)
//通过想法id查询想法
export const getPostById = (id) => http.get('/post/selectPostById?postId='+id)
//更新想法
export const updatePost = (p) => http.post('/post/updatePost',p)
//查询想法通用接口
export const getPost = (p) => http.get('/post/selectPost',{params:p})
//通过浏览量分页查询想法
export const getPostByViews = (pageNum,pageSize) => http.get(`/post/selectPostByViews/${pageNum}/${pageSize}`)
//通过浏览量降序查所有想法（废弃）
export const getPostByView = (p) => http.get('/post/selectPostByView',{params:p})
//通过更新时间降序查所有想法
export const getPostByUptime = (p) => http.get('/post/selectPostByUptime',{params:p})
//通过想法标题查想法
export const getPostByTitle = (p) => http.get('/post/selectPostByPostTitle',{params:p})
//删除想法传postId
export const removePost = (id) => http.post('/post/deletePost/'+id)
//新增想法
export const sendPost = (p) => http.post('/post/insertPost',p)
//浏览量+1
export const viewPost = (id) => http.post('/post/viewPost',{postId:id})
//收藏
export const collectPost = (userId,postId) => http.post(`/post/collectionPost/${userId}/${postId}`)
//取消收藏
export const uncollectPost = (userId,postId) => http.post(`/post/removeCollectionPost/${userId}/${postId}`)
//查询已收藏的所有想法
export const getMyCollectPost = (userid) => http.get('/post/selectCollectionPost/'+userid)
//查询是否收藏
export const hasCollectPost = (userId,postId) => http.get(`/post/hasCollectionPost/${userId}/${postId}`,{custom: {needLogin: true}})
//点赞
export const likePost = (userId,postId) => http.post(`/post/likePost/${userId}/${postId}`)
//取消点赞
export const dislikePost = (userId,postId) => http.post(`/post/removeLikePost/${userId}/${postId}`)
//查询是否收藏
export const hasLikePost = (userId,postId) => http.get(`/post/hasLikePost/${userId}/${postId}`,{custom: {needLogin: true}})
//按照关键词搜索想法
export const getPostBySearch = (p) => http.post(`/post/searchPost`,p)
//查询我关注的人发布的想法
export const getFollowPost = (userId,pageSize,pageNum) => http.get(`/post/selectMyFriendPost/${userId}/${pageSize}/${pageNum}`)
//查询某用户发布的想法
export const getMyPost = (userId,pageNum,pageSize) => http.get(`/post/selectAllPostByUserid/${userId}/${pageNum}/${pageSize}`)
//插入赛事对应的想法
export const addContestPost = (postId,contestId) => http.post(`/contest/addContestPost/${postId}/${contestId}`)
//根据赛事查询对应的想法
export const getPostByContest = (contestId,pageNum,pageSize) => http.get(`/contest/selectContestPost/${contestId}/${pageNum}/${pageSize}`)


//comment（评论）
//查评论通用接口
export const getComment = (p) => http.get('/comment/selectComment',{params:p})
//按点赞数降序
export const getCommentByLike = (p) => http.get('/comment/selectCommentByLike',{params:p})
//发评论
export const sendComment = (p) => http.post('/comment/insertComment',p)
//删除评论
export const removeComment = (id) => http.post('/comment/deleteComment/'+id)
//传入想法id查询所有一级评论 
export const getTopComment = (postId) => http.get('/comment/selectTopComment/'+postId)
//传入一级评论的id查询所有子评论
export const getSubAll  = (faId) => http.get('/comment/selectSubComment/'+faId)
//传入一级评论的id查询最多2个子评论
export const getSubLE2 = (id) => http.get('/comment/selectSubLE2/'+id)
//点赞
export const likeComment = (userId,comId) => http.post(`/comment/likePostComment/${userId}/${comId}`)
//取消点赞
export const dislikeComment = (userId,commentId) => http.comment(`/comment/removeLikePostComment/${userId}/${commentId}`)
//查询是否收藏
export const hasLikePostComment = (userId,commentId) => http.get(`/comment/hasLikePostComment/${userId}/${commentId}`,{custom: {needLogin: true}})
//评论的点赞数
export const getCommentLikeNum = (commentId) => http.get(`/comment/beSelectLikePostCommentNum/${commentId}`)

/*---------------- 比赛模块 ---------------*/
//查询所有比赛信息
export const getAllContest = (p) => http.get(`/contest/selectAll`,p);
//按照热度分页查询比赛
export const getContestOrder = (pageNum,pageSize) => http.get(`/contest/selectByPage/${pageNum}/${pageSize}`);
//按照比赛id分页查询team
export const getTeamByContest = (contestId,pageNum,pageSize) => http.get(`/contest/selectContestTeam/${contestId}/${pageNum}/${pageSize}`);
//添加某个比赛对应的组队需求
export const addContestTeam = (teamId,contestId) => http.get(`/contest/addContestTeam/${teamId}/${contestId}`);
//根据id查询比赛详情
export const getContestById = (teamId) => http.get(`/contest/selectById/${teamId}`);