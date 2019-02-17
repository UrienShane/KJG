package com.urien.kjg.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private String algorithmName = "MD5";   //指定散列算法为MD5,还有别的算法如：SHA256、SHA1、SHA512
    private int hashIterations = 2;     //散列迭代次数 md5(md5(pwd)): new Md5Hash(password, salt, 2).toString()

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    //加密：输入明文得到密文
    public String  encodePassword(String password, String salt) {
        String  crdentials = password;
        String  Password_new = new SimpleHash(algorithmName, crdentials, salt, hashIterations).toHex();
        return Password_new;
    }

    public boolean verifyPassWord(String targetPassword, String password, String salt){
        Object Password_new = this.encodePassword(targetPassword, salt);
        if(Password_new.equals(password)){
            return true;
        }else{
            return false;
        }
    }
}
