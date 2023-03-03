CREATE TABLE `chat` (
  `chat_id` bigint NOT NULL,
  `chat_senderId` int NOT NULL,
  `chat_recipientId` int DEFAULT NULL,
  `char_context` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `chat_time` datetime NOT NULL,
  PRIMARY KEY (`chat_id`),
  KEY `chat_chat_senderId_chat_recipientId_chat_time_index` (`chat_senderId`,`chat_recipientId`,`chat_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `contest` (
  `contest_id` bigint NOT NULL AUTO_INCREMENT,
  `contest_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '比赛标题',
  `contest_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '比赛描述',
  `contest_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `contest_begin_time` datetime DEFAULT NULL COMMENT '更新时间',
  `contest_end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `contest_area` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '赛事地区',
  `contest_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '比赛相关url',
  `contest_view` bigint DEFAULT '0' COMMENT '浏览量',
  `contest_image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片url',
  PRIMARY KEY (`contest_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `contest_post` (
  `contest_post_id` int NOT NULL AUTO_INCREMENT,
  `contest_id` bigint NOT NULL,
  `post_id` bigint NOT NULL,
  PRIMARY KEY (`contest_post_id`),
  KEY `contest_post_ibfk_1` (`contest_id`),
  KEY `contest_post_ibfk_2` (`post_id`),
  CONSTRAINT `contest_post_ibfk_1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `contest_post_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `contest_team` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contest_id` bigint NOT NULL COMMENT '比赛id',
  `team_id` bigint NOT NULL COMMENT '组队需求id',
  PRIMARY KEY (`id`),
  KEY `contest_id` (`contest_id`) USING BTREE,
  KEY `team_id` (`team_id`) USING BTREE,
  CONSTRAINT `contest_team_ibfk_1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `contest_team_ibfk_2` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

CREATE TABLE `event_intro` (
  `user_id` int unsigned NOT NULL,
  `user_resume` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '简介',
  `user_skill` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '擅长技能',
  `user_edu` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '学历教育',
  `user_prize` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '获奖荣誉',
  `user_expr` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '项目经验',
  `user_bgimg` varchar(200) DEFAULT NULL COMMENT '背景图片',
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `fk_user_ev_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='一问用户简介表';

CREATE TABLE `event_level` (
  `userId` int unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `level` int NOT NULL DEFAULT '1' COMMENT '等级',
  `exp` int NOT NULL DEFAULT '0' COMMENT '经验值',
  PRIMARY KEY (`userId`),
  CONSTRAINT `fk_level_userid` FOREIGN KEY (`userId`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='一问等级经验';

CREATE TABLE `post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `post_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子标题',
  `user_id` int unsigned DEFAULT NULL COMMENT '发帖人id',
  `post_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子内容',
  `post_views` bigint NOT NULL DEFAULT '0' COMMENT '浏览量',
  `post_comment_num` bigint NOT NULL DEFAULT '0' COMMENT '评论数',
  `post_time` datetime DEFAULT NULL COMMENT '发帖时间',
  `post_type` varchar(20) NOT NULL COMMENT '帖子类型',
  `post_ip` varchar(20) DEFAULT NULL COMMENT '帖子ip',
  `post_uptime` datetime DEFAULT NULL COMMENT '帖子更新时间',
  `post_like_num` bigint NOT NULL DEFAULT '0' COMMENT '帖子点赞数',
  `post_collect_num` bigint NOT NULL DEFAULT '0' COMMENT '帖子收藏数',
  `post_img_url` text,
  PRIMARY KEY (`post_id`),
  KEY `post_user_id_index` (`user_id`),
  KEY `post_post_views_post_time_post_uptime_index` (`post_views`,`post_time`,`post_uptime`),
  CONSTRAINT `fk_uid` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_urid` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1628787475067297794 DEFAULT CHARSET=utf8mb3 COMMENT='帖子';

CREATE TABLE `post_collect` (
  `post_id` bigint NOT NULL COMMENT '帖子id',
  `user_id` int unsigned NOT NULL COMMENT '收藏人id',
  PRIMARY KEY (`post_id`,`user_id`),
  KEY `fk_pcol_pid` (`post_id`),
  KEY `fk_pcol_uid` (`user_id`),
  CONSTRAINT `fk_pcol_pid` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_pcol_uid` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `post_comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '帖子评论id',
  `user_id` int unsigned DEFAULT NULL COMMENT '评论人id',
  `post_id` bigint NOT NULL COMMENT '所属帖子id',
  `comment_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `comment_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `comment_ip` varchar(255) DEFAULT NULL COMMENT '评论ip',
  `fa_comment_id` bigint DEFAULT NULL COMMENT '父评论id',
  `commend_likes` bigint DEFAULT '0' COMMENT '评论的点赞数',
  PRIMARY KEY (`comment_id`),
  KEY `fk_post_id` (`post_id`),
  KEY `fk_userid` (`user_id`),
  KEY `post_comment_comment_time_commend_likes_index` (`comment_time`) USING BTREE,
  KEY `fa_comment_id` (`fa_comment_id`) USING BTREE,
  CONSTRAINT `fk_post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1613524164179353602 DEFAULT CHARSET=utf8mb3 COMMENT='帖子一级评论';

CREATE TABLE `post_comment_like` (
  `comment_id` bigint NOT NULL COMMENT '评论id',
  `user_id` int unsigned NOT NULL COMMENT '点赞人id',
  PRIMARY KEY (`comment_id`,`user_id`),
  KEY `fk_clike_cid` (`comment_id`),
  KEY `fk_clike_uid` (`user_id`),
  CONSTRAINT `fk_clike_cid` FOREIGN KEY (`comment_id`) REFERENCES `post_comment` (`comment_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_clike_uid` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `post_like` (
  `post_id` bigint NOT NULL COMMENT '帖子id',
  `user_id` int unsigned NOT NULL COMMENT '点赞人id',
  PRIMARY KEY (`post_id`,`user_id`),
  KEY `fk_plike_pid` (`post_id`),
  KEY `fk_plike_uid` (`user_id`),
  CONSTRAINT `fk_plike_pid` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_plike_uid` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `team` (
  `team_id` bigint NOT NULL COMMENT '组队id',
  `team_leader` int unsigned DEFAULT NULL COMMENT '发起人',
  `team_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `team_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详情',
  `team_send_time` datetime NOT NULL COMMENT '组队的发布时间',
  `team_start_time` datetime NOT NULL COMMENT '开始时间',
  `team_end_time` datetime NOT NULL COMMENT '结束时间',
  `team_pnum` smallint NOT NULL COMMENT '所需人数',
  `team_get_pnum` smallint NOT NULL DEFAULT '0' COMMENT '已组人数',
  `team_place` varchar(150) DEFAULT NULL COMMENT '地点',
  `team_code` int unsigned NOT NULL COMMENT '组队码（短一些随机生成）',
  `team_psw` varchar(12) NOT NULL COMMENT '申请组队的密码（队长设置）',
  `team_img_url` text,
  `team_contact` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`team_id`),
  KEY `post_team_ibfk_1` (`team_leader`),
  KEY `team_team_leader_team_send_time_team_end_time_index` (`team_leader`,`team_send_time`,`team_end_time`),
  CONSTRAINT `team_ibfk_1` FOREIGN KEY (`team_leader`) REFERENCES `user` (`userid`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='组队表';

CREATE TABLE `team_collect` (
  `team_id` bigint NOT NULL,
  `user_id` int unsigned NOT NULL,
  PRIMARY KEY (`team_id`,`user_id`),
  KEY `user_id` (`user_id`),
  KEY `team_collect_two` (`team_id`,`user_id`),
  CONSTRAINT `team_collect_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_collect_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='组队收藏';

CREATE TABLE `team_comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `user_id` int unsigned DEFAULT NULL COMMENT '评论人id',
  `team_id` bigint DEFAULT NULL COMMENT '所属组队id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `comment_centent` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `father_id` bigint DEFAULT NULL COMMENT '父评论id',
  `comment_likes` bigint DEFAULT '0' COMMENT '点赞数',
  `delete` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `comment_ip` varchar(255) DEFAULT NULL COMMENT '评论的ip地址',
  PRIMARY KEY (`comment_id`),
  KEY `fk_teamcom_id` (`team_id`),
  KEY `fk_teamuser_id` (`user_id`),
  CONSTRAINT `fk_teamcom_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_teamuser_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34123123422 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `team_join` (
  `join_id` bigint NOT NULL,
  `team_id` bigint NOT NULL COMMENT 'team的id',
  `user_id` int unsigned DEFAULT NULL COMMENT '参与人',
  `join_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`join_id`),
  KEY `fk_user_bxb_id` (`user_id`),
  KEY `fk_team_id` (`team_id`),
  CONSTRAINT `fk_team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_bxb_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='组队情况桥表';

CREATE TABLE `team_like` (
  `team_id` bigint NOT NULL,
  `user_id` int unsigned NOT NULL,
  PRIMARY KEY (`team_id`,`user_id`),
  KEY `fk_team_like_2` (`user_id`),
  KEY `team_like_two` (`team_id`,`user_id`),
  CONSTRAINT `fk_team_like_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_team_like_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='组队点赞';

CREATE TABLE `unread_msg` (
  `user_id` int unsigned NOT NULL COMMENT '用户自己的id',
  `unread_num` bigint NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_unread_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='未读消息';

CREATE TABLE `user` (
  `userid` int unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信登录id',
  `openname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '微信用户' COMMENT '微信昵称',
  `realname` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `edu_account` bigint unsigned DEFAULT NULL COMMENT '智慧交大账号',
  `edu_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '智慧交大密码',
  `edu_enc` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '智慧交大加密后密码',
  `edu_status` int unsigned NOT NULL DEFAULT '0' COMMENT '0为未绑定，1为正常，2为密码错误',
  `updatetime` timestamp NOT NULL DEFAULT '2019-12-31 16:00:00' COMMENT '最后更新时间',
  `lover_status` int unsigned NOT NULL DEFAULT '0' COMMENT '0为未绑定，1为正常，2为待确认',
  `lover_id` int unsigned DEFAULT NULL COMMENT '情侣用户id',
  `avatar` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像地址',
  `gpa` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '绩点',
  `credit_art` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文体艺术学分',
  `credit_vo` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '志愿服务与社会实践学分',
  `credit_work` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '社会工作学分',
  `lover_apply_time` int unsigned DEFAULT NULL COMMENT '情侣申请时间',
  `lover_date` int DEFAULT NULL COMMENT '情侣相恋日期',
  `userid_show` int unsigned DEFAULT NULL COMMENT '用户id（用于展示）',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'user邮箱',
  PRIMARY KEY (`userid`) USING BTREE,
  UNIQUE KEY `edu_account` (`edu_account`) USING BTREE,
  KEY `account_email_index` (`edu_account`,`email`)
) ENGINE=InnoDB AUTO_INCREMENT=113876 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户表';

CREATE TABLE `user_info` (
  `userid` int unsigned NOT NULL COMMENT '用户ID',
  `realname` char(10) NOT NULL COMMENT '真实姓名',
  `class` varchar(100) NOT NULL COMMENT '班级',
  `sex` char(5) NOT NULL COMMENT '性别',
  `nation` char(10) NOT NULL COMMENT '班级',
  `birth` char(20) DEFAULT NULL COMMENT '生日',
  `id` char(20) DEFAULT NULL COMMENT '身份证号',
  `political` varchar(50) DEFAULT NULL COMMENT '政治面貌',
  `hometown` varchar(50) DEFAULT NULL COMMENT '家乡',
  `elevel` char(2) DEFAULT NULL COMMENT '英语等级',
  `timetable` varchar(5000) DEFAULT NULL COMMENT '课表json',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

