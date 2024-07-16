package com.ztt.cloudprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 当引入了其他项目但不是以starter的形式,就不会自动配置需要手动扫描引入的工程下的bean,
 * // @SpringBootApplication(scanBasePackages = {"com.ztt.common","com.ztt.cloudprovider"}) 这样的话还需要加入自己项目需要扫描的包 不会有默认的配置
 * 主要是因为包的开头不一样导致
 * 如果自动配置后就不需要手动扫描包
 *
 * @author ZTT
 */
@EnableFeignClients(basePackages = {"com.ztt.cloudprovider.clouddaointerface"})
// 将cloud-common封装成starter就不需要扫描commom包
@SpringBootApplication(scanBasePackages = {"com.ztt.cloudprovider", "com.ztt.common"})
public class CloudProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudProviderApplication.class, args);
    }

}
