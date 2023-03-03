package com.event.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccessToken {
    private static final Map<String, String> TOKEN_MAP = new ConcurrentHashMap<>();

    private static final String APPID = "wx00a35152ace62c28";
    private static final String APPSECRET = "458d3d5b81a0c56a8d82a91e24689280";

    public static String getAccessToken() throws Exception {
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID
                + "&secret=" + APPSECRET;
        System.out.println("URL for getting accessToken accessTokenUrl=" + accessTokenUrl);

        URL url = new URL(accessTokenUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        // 获取返回的字符
        InputStream inputStream = connection.getInputStream();
        int size = inputStream.available();
        byte[] bs = new byte[size];
        inputStream.read(bs);
        String message = new String(bs, "UTF-8");

        // 获取access_token
        JSONObject jsonObject = JSONObject.parseObject(message);
        String accessToken = jsonObject.getString("access_token");


        TOKEN_MAP.put("accessToken", accessToken);
        return accessToken;
    }
}