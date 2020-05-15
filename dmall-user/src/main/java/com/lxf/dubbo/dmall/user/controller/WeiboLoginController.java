package com.lxf.dubbo.dmall.user.controller;

import com.alibaba.fastjson.JSON;
import com.lxf.dubbo.dmall.user.bo.UmsMember;
import com.lxf.dubbo.dmall.user.service.UserService;
import com.lxf.dubbo.dmall.user.util.HttpclientUtil;
import com.lxf.dubbo.dmall.user.util.JwtUtil;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xufeng.liu
 * @email xueshzd@163.com
 * @date 2020/5/14 15:55
 */
@Controller
public class WeiboLoginController {


    @Autowired
    private UserService userService;


    @RequestMapping("vlogin")
    public String vlogin(String code,HttpServletRequest request){

        // 授权码换取access_token
        // 换取access_token
        // client_secret=13abd33faa52673e7e6ef4010ca25e50
        // client_id=200363871
        String s3 = "https://api.weibo.com/oauth2/access_token?";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","200363871");
        paramMap.put("client_secret","13abd33faa52673e7e6ef4010ca25e50");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
        paramMap.put("code",code);// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);

        Map<String,Object> access_map = JSON.parseObject(access_token_json,Map.class);

        // access_token换取用户信息
        String uid = (String)access_map.get("uid");
        String access_token = (String)access_map.get("access_token");
        String show_user_url = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
        String user_json = HttpclientUtil.doGet(show_user_url);
        Map<String,Object> user_map = JSON.parseObject(user_json,Map.class);



        // 将用户信息保存数据库，用户类型设置为微博用户
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType("2");
        umsMember.setAccessCode(code);
        umsMember.setAccessToken(access_token);
        umsMember.setSourceUid((String)user_map.get("idstr"));
        umsMember.setCity((String)user_map.get("location"));
        umsMember.setNickname((String)user_map.get("screen_name"));
        String g = "0";
        String gender = (String)user_map.get("gender");
        if(gender.equals("m")){
            g = "1";
        }
        umsMember.setGender(g);

        UmsMember umsCheck = new UmsMember();
        umsCheck.setSourceUid(umsMember.getSourceUid());
        UmsMember umsMemberCheck = userService.checkOauthUser(umsCheck);// 检查该用户(社交用户)以前是否登陆过系统

        if(umsMemberCheck==null){
            umsMember = userService.addOauthUser(umsMember);
        }else{
            umsMember = umsMemberCheck;
        }

        // 生成jwt的token，并且重定向到首页，携带该token
        String token = null;
        String memberId = umsMember.getId();// rpc的主键返回策略失效
        String nickname = umsMember.getNickname();
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("memberId",memberId);// 是保存数据库后主键返回策略生成的id
        userMap.put("nickname",nickname);


        String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();// 从request中获取ip
            if(StringUtils.isBlank(ip)){
                ip = "127.0.0.1";
            }
        }

        // 按照设计的算法对参数进行加密后，生成token
        token = JwtUtil.encode("dubboMall", userMap, ip);

        // 将token存入redis一份
        userService.addUserToken(token,memberId);
        return "index";
    }

    @RequestMapping("/hello")
    public String hello(String code,HttpServletRequest request){
        return "login";
    }

    @RequestMapping("/index")
    public String index(String code,HttpServletRequest request){
        return "index";
    }

}
