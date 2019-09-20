package com.yjx.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.yjx.config.Config;
import com.yjx.config.DruidDataSourceConfig;

@SpringBootApplication
@MapperScan("com.yjx.dao")
@Import({Config.class,DruidDataSourceConfig.class})
@ComponentScan("com.yjx.controller")
public class Main extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
        return application.sources(Main.class);
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Main.class, args);

	}

}
