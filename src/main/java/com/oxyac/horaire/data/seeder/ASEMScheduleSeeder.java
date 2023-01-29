package com.oxyac.horaire.data.seeder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ASEMScheduleSeeder {

    @Bean
    public CommandLineRunner seedDB() {
        return (args) -> {
            log.info("SEEDING");


        };
    }
}
