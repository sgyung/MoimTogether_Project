package com.project.moimtogether.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestClientConfig {

    private static final String BASE_URL = "http://openapi.seoul.go.kr:8088";

    @Bean
    public RestClient restClient() {
        // DefaultUriBuilderFactory 설정
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(BASE_URL);
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        return RestClient.builder()
                .uriBuilderFactory(uriBuilderFactory)
                .baseUrl(BASE_URL)
                .build();
    }
}
