package com.example.finsage.config;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        Map<String, Object> envProps = new HashMap<>();
        dotenv.entries().forEach(entry -> envProps.put(entry.getKey(), entry.getValue()));

        // Map dotenv keys to Spring Boot expected keys
        if (dotenv.get("DB_USERNAME") != null) {
            envProps.put("spring.datasource.username", dotenv.get("DB_USERNAME"));
        }

        if (dotenv.get("DB_PASSWORD") != null) {
            envProps.put("spring.datasource.password", dotenv.get("DB_PASSWORD"));
        }

        if (dotenv.get("JWT_SECRET") != null) {
            envProps.put("jwt.secret", dotenv.get("JWT_SECRET"));
        }

        environment.getPropertySources().addFirst(new MapPropertySource("dotenv", envProps));
    }
}