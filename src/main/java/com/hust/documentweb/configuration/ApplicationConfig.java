package com.hust.documentweb.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.config")
@Getter
@Setter
public class ApplicationConfig {
    private String dataStoragePath;
}
