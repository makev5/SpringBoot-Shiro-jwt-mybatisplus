package com.xiaoke.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoke.demo.beans.User;
import com.xiaoke.demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test

	public void getUser() {

		QueryWrapper<User> condition = new QueryWrapper<>();
		condition.eq("username", "admin").last("limit 1");
		Integer integer = userMapper.selectCount(condition);
		if(integer>0) {
			System.out.println("存在");
			String user = userMapper.selectOne(condition).getRole();
			System.out.println(user);
		} else {
			User user = new User();
			System.out.println(user.getUsername());
		}
		
	}

}
