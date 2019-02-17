package com.urien.kjg.realm;

import com.urien.kjg.domain.User;
import com.urien.kjg.service.UserService;
import com.urien.kjg.util.PasswordUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTelephoneNumRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordUtil passwordUtil;

    /*
     * 执行授权逻辑
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthenticationException {
        System.out.println("---执行TelephoneNumRealm授权逻辑---");

        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //添加资源的授权字符串

        //到数据库中查询当前用户的授权字符串
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();

        System.out.println("正在对 " + user.getUsername() + " 授权");
        System.out.println(user.getUsername() + " 的授权码为：" + user.getPerms());

        info.addStringPermission(user.getPerms());

        return info;
    }


    /*
     * 执行认证逻辑
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("---执行UserTelephoneNumRealm认证逻辑---");
        User user = null;

        //编写Shiro判断逻辑，判断用户名和密码
        //1.判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;

        System.out.println("-获取token数据-");
        System.out.println("token账号：" + token.getUsername());
        System.out.println("token密码：" + token.getPassword());

        System.out.println("-获取数据库数据-");
        user = userService.findUserByTelephoneNum(token.getUsername());

        if(user == null){
            //该用户不存在
            throw new UnknownAccountException();
        }

        //加密token获取的密码
        String x = new String(token.getPassword());
        String password_encoded = passwordUtil.encodePassword(x, user.getTelephoneNum());
        token.setPassword(password_encoded.toCharArray());
        System.out.println(x);
        System.out.println(user.getTelephoneNum());
        System.out.println("加密后的密码 = " + password_encoded);

        System.out.println("数据库中的密码：" + user.getPassword());

            //以下信息是从数据库中获取的
            //1)principal：认证的实体信息，可以是username，也可以是数据库表对应的用户的实体对象
        Object principal = user.getUsername();
            //2)credentials：密码
        Object credentials = user.getPassword();
            //3)salt：盐
        ByteSource salt = ByteSource.Util.bytes(user.getTelephoneNum());
            //4)realmName：当前realm对象的name，调用父类的getName()方法即可
        String realmName = getName();

        System.out.println("salt：" + salt);

        //2.判断密码（第一个参数填登录用户名,第二个参数是加密后的登录密码，第三个参数是盐，第四个参数是realm的名字）
        SimpleAuthenticationInfo info =  new SimpleAuthenticationInfo(user, credentials, salt, realmName);

        System.out.println("结果：" + info);

        return info;
    }
}

