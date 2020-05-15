package com.lxf.dubbo.dmall.user.controller;

import com.alibaba.fastjson.JSON;
import com.lxf.dubbo.dmall.user.util.HttpclientUtil;

import java.util.HashMap;
import java.util.Map;

public class TestOauth2 {

    public static String getCode(){

        // 1 获得授权码
        // 200363871
        // http://passport.gmall.com:8085/vlogin

        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=200363871&response_type=code&redirect_uri=http://passport.gmall.com:8085/vlogin");


        System.out.println(s1);

        // 在第一步和第二部返回回调地址之间,有一个用户操作授权的过程
        //http://passport.gmall.com:8085/vlogin?code=2df706e8294c3fbbf53299163e1bba23
        // 2 返回授权码到回调地址

        return null;
    }

    public static String getAccess_token(){
        // 换取access_token
        // client_secret=13abd33faa52673e7e6ef4010ca25e50
        // client_id=200363871
        String s3 = "https://api.weibo.com/oauth2/access_token?";//?client_id=187638711&client_secret=a79777bba04ac70d973ee002d27ed58c&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/vlogin&code=CODE";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","200363871");
        paramMap.put("client_secret","13abd33faa52673e7e6ef4010ca25e50");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
        paramMap.put("code","719d2649c86cb84b0b0789a9bde898d5");// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);

       Map<String,String> access_map = JSON.parseObject(access_token_json,Map.class);

        System.out.println(access_token_json);
       System.out.println(access_map.get("access_token"));
       System.out.println(access_map.get("uid"));

        return access_map.get("access_token");
    }

    public static Map<String,String> getUser_info(){

//        2.00oK6YHH02nhYNee70a65fcfeLIP_C
//        6524564098
//        {"access_token":"2.00oK6YHH02nhYNee70a65fcfeLIP_C","remind_in":"148320","expires_in":148320,"uid":"6524564098","isRealName":"true"}

        // 4 用access_token查询用户信息                                           2.00oK6YHH02nhYNee70a65fcfeLIP_C
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00hBLaRD02nhYNd79db99251k6nRiD&uid=3008218405";
        String user_json = HttpclientUtil.doGet(s4);
        Map<String,String> user_map = JSON.parseObject(user_json,Map.class);

        System.out.println(user_json);
        System.out.println(String.valueOf(user_map.get("screen_name")));
        System.out.println(String.valueOf(user_map.get("id")));

        return user_map;
    }


    public static void main(String[] args) {
        //1、获取授权码code的值
//        getCode();
        //2、获取token
//        getAccess_token();
        //3、获取用户信息
        getUser_info();

    }
}
