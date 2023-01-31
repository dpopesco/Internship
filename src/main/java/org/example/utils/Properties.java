package org.example.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)

@Configuration
@PropertySource("classpath:config.properties")
public class Properties {

    @Value("${project.scheme}")
    private String scheme;

    @Value("${project.server}")
    private String server;

    @Value("${project.basePath}")
    private String basePath;

    @Value("${project.appId}")
    private String appId;

    public String getAppUrl() {
        return String.format("%s://%s/%s", getScheme(), getServer(), getBasePath());
    }
}
