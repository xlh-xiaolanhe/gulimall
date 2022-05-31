package com.xiaolanhe.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/30 21:01
 */

@Configuration
public class MyRedissonConfig {

    // 所有对Redisson的使用都是通过RedissonClient对象
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("192.168.235.132:6379");
        return Redisson.create(config);
    }
}
