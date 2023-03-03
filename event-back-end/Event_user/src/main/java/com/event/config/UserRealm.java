package com.event.config;

import com.event.pojo.User;
import com.event.service.UserService;
import com.event.utils.JwtToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    @Override  //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override  //认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String)token.getPrincipal();
        String userId = JwtToken.isToken(accessToken);
        User user = userService.selectUserById(Integer.parseInt(userId));
        if (user==null) return null;
        return new SimpleAuthenticationInfo(user,accessToken,"");
    }
}
