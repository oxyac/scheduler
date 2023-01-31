package com.oxyac.horaire.config;

import com.oxyac.horaire.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class BotAppConfig {

    private final TelegramBot telegramBot;

    public BotAppConfig(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            log.info("STARTED_DEMO");
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(telegramBot);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        };
    }

}
