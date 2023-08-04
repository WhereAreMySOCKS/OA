package com.cnic.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication // 默认扫描主程序所在的包及其子包，因此需要下面的注解
@ComponentScan("com.cnic") // 设置包的默认扫描规则
public class ServiceAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class);
    }
}
