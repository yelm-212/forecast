package com.bigs.forecast.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    @Getter
    @Value("${openapi.key}")
    private String forecastKey;

    @Getter
    @Value("${openapi.origin}")
    private String originKey;

}
