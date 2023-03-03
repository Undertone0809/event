package com.event.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Sha1 {
    //静态方法，返回sha1加密后的结果
    public static String getSha1(String password){
        return DigestUtils.sha1Hex(password);
    }
}
