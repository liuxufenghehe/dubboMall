package com.lxf.dubbo.dmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.lxf.dubbo.dmall.user.mapper")
public class DmallUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmallUserApplication.class, args);
	}

}
