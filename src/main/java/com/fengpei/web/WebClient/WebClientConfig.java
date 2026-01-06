package com.fengpei.web.WebClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                //.baseUrl("http://49.235.146.115:9511/api") // 外部服务基础 URL
                .baseUrl("http://www.jpsw99.com") // 外部服务基础 URL
                .build();
    }
}
