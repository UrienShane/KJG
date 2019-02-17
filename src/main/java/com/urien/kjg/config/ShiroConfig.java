package com.urien.kjg.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
//import com.urien.kjg.realm.CustomModularRealmAuthenticator;
//import com.urien.kjg.realm.TelephoneRealm;
import com.urien.kjg.realm.UserTelephoneNumRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ShiroConfig {

    /*
    * 创建ShiroFiterFactoryBean
    * */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //shiroFilterFactoryBean需要关联securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //配置登录的url和登录成功的url以及验证失败的url
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        shiroFilterFactoryBean.setSuccessUrl("/home");
        shiroFilterFactoryBean.setUnauthorizedUrl("/toLogin");
        //配置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

        //添加Shiro内置过滤器
        /*
         * Shiro内置过滤器可以实现权限相关的拦截
         *   常用的过滤器：
         *       anon：无需认证，即可访问
         *       authc：必须认证才能访问
         *       user：如果使用rememMe的功能可以直接访问
         *       perms：该资源必须得到资源权限才能访问
         *       role：该资源必须得到角色权限才能访问
         * */
        Map<String, String> filterMap = new LinkedHashMap<String, String>();

        filterMap.put("/home", "anon");
        filterMap.put("/register", "anon");
        filterMap.put("/test", "anon");
        filterMap.put("/update", "anon");
        //放行login.html
        filterMap.put("/login", "anon");

        //授权过滤器
        //当前授权拦截后，shiro会自动跳转到未授权页面
        //perms[授权码]
        filterMap.put("/vipSee", "perms[VIP]");
        filterMap.put("/vipClick", "perms[VIP]");

        //这条过滤器必须放到最后，否则会拦截所有请求
        filterMap.put("/*", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        System.out.println("---Shiro拦截工厂注入成功---");
        return shiroFilterFactoryBean;
    }

    /*
    * 创建DefaultWebSecurityManager
    * */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userTelephoneNumRealm") UserTelephoneNumRealm userTelephoneNumRealm){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //securityManager需要关联realm
        securityManager.setRealm(userTelephoneNumRealm);
        return securityManager;
    }


    /*
    * 创建Realm
    * */
    @Bean(name = "userTelephoneNumRealm")
    public UserTelephoneNumRealm userTelephoneNumRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher hashedCredentialsMatcher){
        //告诉realm,使用credentialsMatcher加密算法类来验证密文
        UserTelephoneNumRealm userTelephoneNumRealm = new UserTelephoneNumRealm();
        userTelephoneNumRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return new UserTelephoneNumRealm();
    }

    /*
    * 因为密码是加密过的，如果要Shiro验证用户身份，需要告诉它我们用的是md5加密的，并且是加密了两次。
    * 在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
    * */
    @Bean(name="hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //加密方式
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //加密次数
        hashedCredentialsMatcher.setHashIterations(2);
        //存储散列后的密码是否为16进制
        return hashedCredentialsMatcher;
    }


    /*
    * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
    * */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
