package com.oxyac.horaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.oxyac.horaire")
@EnableScheduling
@EnableAsync
public class HoraireApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoraireApplication.class, args);
    }

}
