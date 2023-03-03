package com.event.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;


/**
 * shiro配置文件!!
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
//        filterMap.put("/post/insertPost", "authc");
//        filterMap.put("/post/collectionPost", "authc");
//        filterMap.put("/oss/uploadfile", "authc");
//        filterMap.put("/unreadMsg/clearUnreadMsg", "authc");
//        filterMap.put("/user/collectionUser", "authc");
//        filterMap.put("/user/updateUser", "authc");
//        filterMap.put("/post/deletePost", "authc");
//        filterMap.put("/unreadMsg/selectUnreadMsg", "authc");
//        filterMap.put("/study/deleteStudySrc", "authc");
//        filterMap.put("/study/insertStudySrc", "authc");
//        filterMap.put("/study/updateStudySrc", "authc");
//        filterMap.put("/comment/LikeComment", "authc");
//        filterMap.put("/comment/deleteComment", "authc");
//        filterMap.put("/comment/insertPost", "authc");
//        shiroFilterFactoryBean.setLoginUrl("/login.html");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加Shiro内置过滤器

        return shiroFilterFactoryBean;
    }
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }
    @Bean(name="userRealm")
    public UserRealm getRealm(){
        return new UserRealm();
    }
}
