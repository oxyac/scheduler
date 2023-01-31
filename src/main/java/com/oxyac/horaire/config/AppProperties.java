package com.oxyac.horaire.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "bot", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class AppProperties {

    @NotNull
    private String name;
    @NotNull
    private String token;
}
