package com.event.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.form.InitUserForm;
import com.event.config.OAuth2Token;
import com.event.form.SearchUserForm;
import com.event.pojo.User;
import com.event.service.UserCollectService;
import com.event.service.UserService;
import com.event.utils.EmileSend;
import com.event.utils.JwtToken;
import com.event.utils.Sha1;
import com.event.vo.TeamInfo;
import com.event.vo.UserCollectInfo;
import com.event.vo.UserDetailVo;
import com.event.vo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 孤雏
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*",maxAge = 3600)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EmileSend emileSend;
    @Autowired
    private UserCollectService userCollectService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${wx.appid}")
    private String AppId;
    @Value("${wx.secret}")
    private String AppSecret;
    /**
     * @return 用户登录模块
     */
    @GetMapping("/login")
    public Result<User> login(User user) {
        //先sha1加密，再md5加密
        String sha1 = Sha1.getSha1(user.getEduPassword());
        user.setEduPassword(DigestUtils.md5DigestAsHex(sha1.getBytes()));
        List<User> users = userService.selectUser(user);
        if (users.size() < 1) return Result.error(ResultCodeEnum.PASSWORDORUSERNAMEERROR);
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new OAuth2Token(JwtToken.createToken(String.valueOf(users.get(0).getUserid())));
        try {
            subject.login(token);
            return Result.ok_data(users.get(0), ResultCodeEnum.SUCCESS, 1);
            //登录成功
        } catch (UnknownAccountException e) {
            //登录失败:用户名不存在
            return Result.error(ResultCodeEnum.USERNAMENOTFOUND);
        } catch (IncorrectCredentialsException e) {
            //登录失败:密码错误
            return Result.error(ResultCodeEnum.PASSWORDERROR);
        }
    }
    /**
     * @return 绑定微信个人信息
     */
    @PostMapping("/bindWxInfo/{userId}")
    public Result<User> wxLogin(@PathVariable Integer userId,@RequestBody InitUserForm initUserForm) {
        if(null != initUserForm){
            User user = userService.selectUserById(userId)
                    .setAvatar(initUserForm.getAvatarUrl())
                    .setOpenname(initUserForm.getNickname());
            if(userService.updateUser(user) > 0){
                return Result.ok_data(user, ResultCodeEnum.SUCCESS, 1);
            }else{
                return Result.error(ResultCodeEnum.MODIFYERROR);
            }
        }else return Result.error(ResultCodeEnum.PARAMETERERROR);
    }

    /**
     * @return 微信登录
     */
    @PostMapping("/wxLogin/{code}")
    public Result<User> wxLogin(@PathVariable("code") String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + AppId +
                "&secret=" + AppSecret +
                "&js_code=" + code +
                "&grant_type=authorization_code";
        String jsonData = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        if (StringUtils.contains(jsonData, "errcode")) return Result.error(ResultCodeEnum.NotONLINE);
        String openid = jsonObject.getString("openid");
        User user = userService.selectUserByOpenId(openid);
        if (user != null){
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new OAuth2Token(JwtToken.createToken(String.valueOf(user.getUserid())));
            try {
                subject.login(token);
                return Result.ok_data(user, ResultCodeEnum.SUCCESS, 1);
            } catch (Exception e) {
                return Result.error(ResultCodeEnum.SYSTEMERROR);
            }
        }
        user = new User();
        long account = (long) ((Math.random() * 9 + 1) * 100000);
        user.setOpenname("一问用户"+ (account))
                .setAvatar("https://tva2.sinaimg.cn/large/0061jZbely8gteby2ezyoj30b40b4mxh.jpg")
                .setOpenid(openid)
                .setEduAccount(account)
                .setEduPassword(DigestUtils.md5DigestAsHex(Sha1.getSha1("123456").getBytes()))
                .setCreatetime(new Date())
                .setUpdatetime(new Date());
        if (userService.insertUser(user)>0){
            user.setIsNewUser(true);
            return Result.ok_data(user,ResultCodeEnum.SUCCESS,1);
        } else return Result.error(ResultCodeEnum.MODIFYERROR);
    }

    /**
     * @return 用户绑定微信号
     */
    @PostMapping("/bindWx/{userid}/{code}")
    public Result<Void> bindWx(@PathVariable("userid") Integer userid,@PathVariable("code") String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + AppId +
                "&secret=" + AppSecret +
                "&js_code=" + code +
                "&grant_type=authorization_code";
        String jsonData = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        if (StringUtils.contains(jsonData, "errcode")) return Result.error(ResultCodeEnum.NotONLINE);
        String openid = jsonObject.getString("openid");
        User user = userService.selectUserById(userid);
        user.setUserid(userid).setOpenid(openid);
        if (userService.updateUser(user) < 1) return Result.error(ResultCodeEnum.MODIFYERROR);
        return Result.ok(ResultCodeEnum.SUCCESS);
    }
    /**
     *
     * @return 根据关键词搜索分页用户
     */
    @PostMapping("/searchUserByPage")
    public Result<List<UserDetailVo>> searchUserByPage(@RequestBody SearchUserForm searchUserForm){
        List<UserDetailVo> list = userService.searchUserInfoByKeywordAndPage(searchUserForm);
        return Result.ok_data(list, ResultCodeEnum.SUCCESS, list.size());
    }

    /**
     * 根据userid分页推荐用户
     */
    @GetMapping("/recommendUser/{userId}/{pageNum}/{pageSize}")
    public Result<List<UserDetailVo>> recommendUser(@PathVariable("userId") Integer userId,
            @PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize){
        List<UserDetailVo> list = userService.recommendUserByActivity(userId,pageNum,pageSize);
        return Result.ok_data(list, ResultCodeEnum.SUCCESS, list.size());
    }

    /**
     * 查找同校用户
     */
    @GetMapping("/findSchoolmate/{userId}/{pageNum}/{pageSize}")
    public Result<List<UserDetailVo>> findSchoolmate(@PathVariable("userId") Integer userId,
                                                     @PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize){
        List<UserDetailVo> list = userService.findSchoolmate(userId,pageNum,pageSize);
        return Result.ok_data(list, ResultCodeEnum.SUCCESS, list.size());
    }

    /**
     * 分页查询我关注的用户的具体信息
     */
    @GetMapping("/selectMyFollower/{userId}/{pageNum}/{pageSize}")
    public Result<List<UserDetailVo>> selectMyFollower(@PathVariable("userId") Integer userId,
           @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize){
        List<UserDetailVo> list = userService.selectMyFollower(userId,pageNum,pageSize);
        return Result.ok_data(list, ResultCodeEnum.SUCCESS, list.size());
    }

    /**
     * 分页查询我的粉丝的具体信息
     */
    @GetMapping("/selectMyFans/{userId}/{pageNum}/{pageSize}")
    public Result<List<UserDetailVo>> selectMyFans(@PathVariable("userId") Integer userId,
                                                       @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize){
        List<UserDetailVo> list = userService.selectMyFans(userId,pageNum,pageSize);
        return Result.ok_data(list, ResultCodeEnum.SUCCESS, list.size());
    }


    /**
     *
     * @return 根据vo查询单个人的个人信息
     */
    @GetMapping("/selectUserInfo/{userid}")
    public Result<UserInfo> selectUserInfo(@PathVariable("userid")Integer userid){
            UserInfo  userinfos = userService.selectUserInfo(userid);
        return userinfos == null ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(userinfos,ResultCodeEnum.SUCCESS,1);
    }

    /**
     *
     * @return 根据条件查询用户信息
     */
    @GetMapping("/selectUser")
    public Result<List<User>> selectUser(User user){
            List<User> users = userService.selectUser(user);
        return users == null ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(users,ResultCodeEnum.SUCCESS,users.size());
    }
    /**
     *
     * @return 根据学号查询具体某人的所有信息,登录后调用放入缓存
     */
    @GetMapping("/selectUserByCode/{eduAccount}")
    public Result<User> selectUserByCode(@PathVariable("eduAccount") Long eduAccount){
            User  user = userService.selectUserByCode(eduAccount);
        return user == null ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(user,ResultCodeEnum.SUCCESS,1);
    }
    /**
     *
     * @return 根据条件修改用户信息
     */
    @PostMapping("/updateUser")
    public Result<Void> updateUser(@RequestBody User user){
            if (userService.updateUser(user)>0) return Result.ok();
            else return Result.error(ResultCodeEnum.MODIFYERROR);
    }
    /**
     *
     * @return 删除用户缓存
     */
    @GetMapping("/clearRedisUser")
    @CacheEvict(value = "User", allEntries=true)
    public Result<Void> clearRedisUser(){
        return Result.ok();
    }
    /**
     *
     * @return 查询用户关注数
     */
    @GetMapping("/selectCollectionUserNum/{collectionUserId}")
    public Result<Integer> selectCollectionUserNum(@PathVariable("collectionUserId")Integer collectionUserId){
        Integer num = userCollectService.selectCollectionUserNum(collectionUserId);
        return Result.ok_data(num,ResultCodeEnum.SUCCESS,num);
    }

    /**
     *
     * @return 查询用户粉丝数
     */
    @GetMapping("/beSelectCollectionUserNum/{beCollectionUserId}")
    public Result<Integer> beSelectCollectionUserNum(@PathVariable("beCollectionUserId")Integer beCollectionUserId){
        Integer num = userCollectService.beSelectCollectionUserNum(beCollectionUserId);
        return Result.ok_data(num,ResultCodeEnum.SUCCESS,num);
    }

    /**
     *
     * @return 查询是否关注，只需要2个用户id
     */
    @GetMapping("/hasCollectionUser/{collectionUserId}/{beCollectionUserId}")
    public Result<Boolean> hasCollectionUser(@PathVariable Integer collectionUserId,@PathVariable Integer beCollectionUserId){
        Boolean hasCollected = userCollectService.hasCollectionUser(collectionUserId, beCollectionUserId) != null;
        return Result.ok_data(hasCollected,ResultCodeEnum.SUCCESS,1);
    }

    /**
     *
     * @return 关注用户接口,只需要2个用户id
     */
    @PostMapping("/collectionUser/{collectionUserId}/{BeCollectionUserId}")
    public Result<Void> collectionUser(@PathVariable("collectionUserId")Integer collectionUserId
            ,@PathVariable("BeCollectionUserId")Integer BeCollectionUserId){
        if (userCollectService.collectionUser(collectionUserId,BeCollectionUserId)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.ERROR);
    }
    /**
     *
     * @return 查看用户的关注接口
     */
    @GetMapping("/selectCollectionUser/{collectionUserId}")
    public Result<List<UserCollectInfo>> selectCollectionUser(@PathVariable("collectionUserId")Integer collectionUserId){
        List<UserCollectInfo> collectionUser = userCollectService.selectCollectionUser(collectionUserId);
        return Result.ok_data(collectionUser,ResultCodeEnum.SUCCESS,collectionUser.size());
    }
    /**
     *
     * @return 查看用户的粉丝接口
     */
    @GetMapping("/beSelectCollectionUser/{beCollectionUserId}")
    public Result<List<UserCollectInfo>> beSelectCollectionUser(@PathVariable("beCollectionUserId")Integer beCollectionUserId){
        List<UserCollectInfo> beSelectCollectionUser = userCollectService.beSelectCollectionUser(beCollectionUserId);
        return Result.ok_data(beSelectCollectionUser,ResultCodeEnum.SUCCESS,beSelectCollectionUser.size());
    }
    /**
     *
     * @return 取消好友关注接口collectionUserId关注者BeCollectionUserId被关注者
     */
    @PostMapping("/removeCollectionUser/{collectionUserId}/{BeCollectionUserId}")
    public Result<Void> removeCollectionUser(@PathVariable("collectionUserId")Integer collectionUserId
            ,@PathVariable("BeCollectionUserId")Integer BeCollectionUserId){
        if (userCollectService.removeCollectionUser(collectionUserId,BeCollectionUserId)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.ERROR);
    }
    /**
     *
     * @return 传经纬度和用户id获取附近的人,进入小程序时务必调用!
     */
    @PostMapping("/nearPeople/{userId}/{longitude}/{latitude}")
    public Result<Map<Object, Object>> nearPeople(@PathVariable("userId")Integer userId
            ,@PathVariable("longitude")double longitude,@PathVariable("latitude")double latitude){
        Map<Object, Object> map = new ConcurrentHashMap<>();
        List<UserInfo> users = new ArrayList<>();
        redisTemplate.opsForGeo().add("nearPeople",new Point(longitude,latitude),userId.toString());
        Distance distance = new Distance(2000, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(100);
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius("nearPeople",userId.toString(),distance,args);
        assert results != null;
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = results.getContent();
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult : content)
                if (Integer.parseInt(geoResult.getContent().getName())!=userId)
                users.add(userService.selectUserInfo(Integer.parseInt(geoResult.getContent().getName())));
        map.put("distance",content);
        map.put("userInfo",users);
        return Result.ok_data(map,ResultCodeEnum.SUCCESS,users.size());
    }
    /**
     *
     * @return 传经纬度和用户id获取附近的组队信息,进入小程序时务必调用!
     */
    @PostMapping("/nearTeam/{userId}/{longitude}/{latitude}/{number}/{pagesize}")
    public Result<Map<Object, Object>> nearTeam(@PathVariable("userId")Integer userId
            ,@PathVariable("longitude")double longitude,@PathVariable("latitude")double latitude,@PathVariable("number")Integer number,@PathVariable("pagesize")Integer pagesize){
        List<TeamInfo> infoTeam = new ArrayList<>(); // 用来存取最后的结果
        Map<Object, Object> map = new ConcurrentHashMap<>();
        List<UserInfo> users = new ArrayList<>();
        redisTemplate.opsForGeo().add("nearPeople",new Point(longitude,latitude),userId.toString());
        Distance distance = new Distance(2000, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(100);
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius("nearPeople",userId.toString(),distance,args);
        assert results != null;
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = results.getContent();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult : content)
            if (Integer.parseInt(geoResult.getContent().getName())!=userId){
                //通过附近的人的id去查询其组队信息
                List<TeamInfo> teamInfos = userService.selectTeam(Long.valueOf(geoResult.getContent().getName())); // 查出该用户所参与的组队
                users.add(userService.selectUserInfo(Integer.parseInt(geoResult.getContent().getName())));
                infoTeam.addAll(teamInfos);
            }
        Collections.sort(infoTeam); // 按时间排序
        ArrayList<TeamInfo> resultInfos = new ArrayList<>();
        for (int i = number, k = 0; (i < number + pagesize) && i < infoTeam.size(); i++,k++)
            resultInfos.add(k,infoTeam.get(i));
        map.put("teamInfo",resultInfos);
        map.put("distance",content);
        return Result.ok_data(map,ResultCodeEnum.SUCCESS,users.size());
    }



    /**
     *
     * @return 点击发送邮件,随机生成验证码发送到指定邮箱
     */
    @PostMapping("/toUpdateEmail/{userid}/{email}")
    public Result<Void> toUpdateEmail(@PathVariable("userid") Integer userid
            ,@PathVariable("email") String email) throws MessagingException {
        String random = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        redisTemplate.opsForValue().set("yzm"+userid,random, 15, TimeUnit.MINUTES);
        emileSend.Send(email,random);
        return Result.ok();
    }

    /**
     *
     * @return 验证验证码是否正确如果正确则修改
     */
    @PostMapping("/updateEmail/{userid}/{email}/{random}")
    public Result<Void> updateEmail(@PathVariable("userid") Integer userid
            ,@PathVariable("email") String email,@PathVariable("random")String random){
        String str = (String) redisTemplate.opsForValue().get("yzm" + userid);
        if (random.equals(str)){
            User user = new User().setUserid(userid).setEmail(email);
            if (userService.updateUser(user)>0) return Result.ok();
            else return Result.error(ResultCodeEnum.MODIFYERROR);
        }else return Result.error(ResultCodeEnum.YZMERROR);
    }
    /**
     *
     * @return 去通过邮箱注册账户,点击发送邮件,随机生成验证码发送到指定邮箱
     */
    @PostMapping("/toInsertUserByEmail/{email}")
    public Result<Void> toInsertUserByEmail(@PathVariable String email) throws MessagingException{
        User user = new User();user.setEmail(email);
        if (userService.selectUser(user).size() > 0) return Result.error(ResultCodeEnum.EMAILERROR);
        String random = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        redisTemplate.opsForValue().set("yzm"+email,random, 15, TimeUnit.MINUTES);
        emileSend.Send(email,random);
        return Result.ok();
    }
    /**
     *
     * @return 验证验证码是否正确如果正确则进行账户注册
     */
    @PostMapping("/insertUserByEmail/{email}/{eduPassword}/{random}")
    public Result<User> insertUserByEmail(@PathVariable String email
            ,@PathVariable String eduPassword,@PathVariable String random){
        String str = (String) redisTemplate.opsForValue().get("yzm" + email);
        if (random.equals(str)){
            User user = new User();
            user.setEmail(email)
                    .setOpenname("一问用户"+random)
                    .setAvatar("https://tva2.sinaimg.cn/large/0061jZbely8gteby2ezyoj30b40b4mxh.jpg")
                    .setEduPassword(DigestUtils.md5DigestAsHex(Sha1.getSha1(eduPassword).getBytes()))
                    .setCreatetime(new Date())
                    .setUpdatetime(new Date())
                    .setEduStatus(0);
            if (userService.insertUser(user)>0){
                user.setIsNewUser(true);
                return Result.ok_data(user,ResultCodeEnum.SUCCESS,1);
            } else return Result.error(ResultCodeEnum.MODIFYERROR);
        }else return Result.error(ResultCodeEnum.YZMERROR);
    }
}

