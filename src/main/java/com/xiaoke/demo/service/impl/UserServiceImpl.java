package com.xiaoke.demo.service.impl;

import com.xiaoke.demo.beans.User;
import com.xiaoke.demo.mapper.UserMapper;
import com.xiaoke.demo.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ma Ke
 * @since 2020-04-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUser(String username) {
        QueryWrapper<User> condition = new QueryWrapper<>();
		condition.eq("username", username).last("limit 1");
		Integer integer = userMapper.selectCount(condition);
		if(integer>0) {
			User user = userMapper.selectOne(condition);
			return user;
		} else {
			return null;
        }
	}
	/**
	 * 用户名是否存在
	 */
	public Boolean isExistUsername(String username) {
		QueryWrapper<User> condition = new QueryWrapper<>();
		condition.eq("username", username).last("limit 1");
		Integer integer = userMapper.selectCount(condition);
		if(integer>0) {
			return true;
		} else {
			return false;
        }
	}
}
