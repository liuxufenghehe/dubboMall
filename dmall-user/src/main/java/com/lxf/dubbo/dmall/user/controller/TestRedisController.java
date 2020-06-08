package com.lxf.dubbo.dmall.user.controller;

import com.lxf.dubbo.dmall.user.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author xufeng.liu
 * @email xueshzd@163.com
 * @date 2020/6/5 16:37
 */
@Controller
public class TestRedisController {

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("/queryKeysCount")
    public String getKeysCount(){
        Jedis jedis = redisUtil.getJedis();
        Long aLong = jedis.dbSize();
        Set<String> keys = jedis.keys("test*");
        List<String> values = new ArrayList<>();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = jedis.get(key);
            values.add(value);
        }
        System.out.println("key的数量："+aLong+";test*的数量："+ keys.size()+";在线用户："+values.toString());
        return "index";
    }



}
