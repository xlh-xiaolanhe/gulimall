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
 *
*
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
