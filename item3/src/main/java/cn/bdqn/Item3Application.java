package cn.bdqn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//扫描映射
@MapperScan("cn.bdqn.dao")
@SpringBootApplication
@ServletComponentScan("cn.bdqn.listener")
public class Item3Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Item3Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Item3Application.class);
    }
}

