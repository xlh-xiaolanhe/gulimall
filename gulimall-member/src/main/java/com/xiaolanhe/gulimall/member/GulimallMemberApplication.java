package com.xiaolanhe.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
   如何远程调用别的服务
        1. 引入open-feign
        2. 编写一个接口，告诉SpringCloud这个接口需要调用远程服务
        3. 声明接口的每一个方法都是调用哪个远程服务的哪个请求
        4. 开启远程调用功能
*/
@RefreshScope
@EnableFeignClients(basePackages = "com.xiaolanhe.gulimall.member.feign")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }

}
