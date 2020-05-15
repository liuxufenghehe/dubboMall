package com.lxf.dubbo.dmall.user.service;

import com.lxf.dubbo.dmall.user.bo.UmsMember; /**
 * @author xufeng.liu
 * @email xueshzd@163.com
 * @date 2020/5/14 16:17
 */
public interface UserService {

    
    UmsMember checkOauthUser(UmsMember umsCheck);

    UmsMember addOauthUser(UmsMember umsMember);

    void addUserToken(String token, String memberId);
}
