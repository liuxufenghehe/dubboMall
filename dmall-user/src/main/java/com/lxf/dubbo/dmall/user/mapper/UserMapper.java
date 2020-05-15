package com.lxf.dubbo.dmall.user.mapper;

import com.lxf.dubbo.dmall.user.bo.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<UmsMember>{

    List<UmsMember> selectAllUser();

}
