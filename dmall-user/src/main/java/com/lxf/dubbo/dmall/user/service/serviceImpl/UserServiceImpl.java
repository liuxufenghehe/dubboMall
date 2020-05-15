package com.lxf.dubbo.dmall.user.service.serviceImpl;

import com.lxf.dubbo.dmall.user.bo.UmsMember;
import com.lxf.dubbo.dmall.user.mapper.UserMapper;
import com.lxf.dubbo.dmall.user.service.UserService;
import com.lxf.dubbo.dmall.user.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * @author xufeng.liu
 * @email xueshzd@163.com
 * @date 2020/5/14 16:18
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UmsMember checkOauthUser(UmsMember umsCheck) {
        UmsMember umsMember = userMapper.selectOne(umsCheck);
        return umsMember;
    }

    @Override
    public UmsMember addOauthUser(UmsMember umsMember) {
        userMapper.insertSelective(umsMember);
        return umsMember;
    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();

        jedis.setex("user:"+memberId+":token",60*60*2,token);

        jedis.close();
    }
}
