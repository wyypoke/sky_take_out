package com.sky.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // 标记这是一个配置类
public class RestTemplateConfig {

    /**
     * 定义 RestTemplate Bean
     * @return 配置好的 RestTemplate 实例
     */
    @Bean
    public RestTemplate restTemplate() {
        // 您可以在这里进行额外的配置，例如设置超时时间、消息转换器等
        return new RestTemplate();
    }
}
