package com.xiaolanhe.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
*  1、整合Mybatis-Plus
*       a、导入依赖
*       b、配置
*           1、数据源
*           2、在application.yml 配置数据源相关信息
*
*       c、配置Mybatis-plus：
*           1、使用@MapperScan
*           2、告诉Mybatis-plus，sql映射文件位置
*
*  2、逻辑删除
*   a. 配置全局的逻辑删除规则
*   b. 配置逻辑删除的组件Bean
*           高版本原因，上述两步可以省略
*   c. 给实体类的逻辑删除字段上加上注解 @TableLogic
*
*    3、JSR303
 *   	1、给Bean添加校验注解: javax.validation.constraints
 *      2、开启校验功能。
 *             效果：校验错误以后会有默认的响应
 *      3、给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 *
 *      4、分组校验
 *          a、给校验注解标注什么情况需要进行校验
 *          b、@Validated({AddGroup.class})
 *          c、默认没有指定分组的校验注解@NotBlank,在分组校验情况下不生效，只会在@Validated生效
 *
 *      5、自定义校验注解
 *          a、编写一个自定义的校验注解
 *          b、编写一个自定义的校验器
 *          c、关联自定义的校验器和自定义的校验注解
 *          @Documented
            @Constraint(
                    validatedBy = {ListValueConstraintValidator.class 可以指定多个不同校验器，匹配不同的情况}
            )
            @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
            @Retention(RetentionPolicy.RUNTIME)
            public @interface ListValue
 *
 *   4、统一的异常处理
 *        @ControllerAdvice
 *      a. 编写异常处理类，使用ControllerAdvice
 *      b. 使用@ExceptionHandler标注方法可以处理异常
 *
 *   5、模板引擎
 *      a.thymeleaf-starter：关闭缓存
 *      b. 静态资源都放在static文件夹下就可以按照路径直接访问
 *      c. 页面放在template下，直接访问
 *          SpringBoot访问项目时，默认会找index.html
 *      d.页面修改后在不重启服务器条件下实时更新(静态资源)
 *          1.引入dev-tools，option标签设置为true
 *          2. 修改完页面后，ctrl + shift + F9
 *
 *    6、整合redis
 *      1.引入data-redis-starter
 *      2.简单配置redis的host等信息
 *      3.使用SpringBoot自动配置好的StringRedisTemplate来操作redis
 *
 *    7、整合redisson作为分布式锁等框架
 *      a、引入依赖
 *          <dependency>
                <groupId>org.redisson</groupId>
                <artifactId>redisson</artifactId>
                <version>3.12.0</version>
            </dependency>
*       b、配置
*
*     8、整合SpringCache简化缓存开发
*          1.引入依赖
*               spring-boot-starter-cache   spring-boot-starter-data-redis
*          2.配置
*               a、自动配置了哪些？
*                   CacheAutoConfiguration 会导入RedisCacheConfiguration
*                   自动配置好缓存管理器 RedisCacheManager
*
*               b、配置使用Redis作为缓存
*          3、注解开发：
*               @Cacheable： 触发将数据保存到缓存的操作
*               @CacheEvict: 触发将数据从缓存删除的操作
*               @CachePut: 不影响方法执行的更新缓存操作
*               @Caching:   组合多个操作
*               @CacheConfig:   在类级别共享相同的缓存的相同配置
*
*           开启缓存功能: @EnableCaching
*
*          4、原理:
*              CacheAutoConfiguration 会导入 RedisCacheConfiguration,
*           而RedisCacheConfiguration自动配置了RedisCacheManager —> 初始化所有的缓存 -> 每个缓存决定使用什么配置
*           -> 如果redisCacheConfiguration有就用已有的，没有就采用默认配置 -> 想更改缓存的配置,只需要给容器中放一个
*           RedisCacheConfiguration即可 -> 就会应用到当前RedisCacheManager管理的所有缓存分区中
* */

@MapperScan("com.xiaolanhe.gulimall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xiaolanhe.gulimall.product.feign")
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }



}
