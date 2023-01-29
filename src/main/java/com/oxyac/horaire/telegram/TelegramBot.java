package com.oxyac.horaire.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "orar_lectii_bot";
    }

    @Override
    public String getBotToken() {
        return "6194121912:AAEN1-0VES-J5AeykyBfWhQbkH-JOSrARcU";
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Got message: {}", update.toString());
        if (update.hasMessage()){
            var msg = update.getMessage();
            var chatId = msg.getChatId();
            try {
                var reply = "DEMO";
                sendNotification(String.valueOf(chatId), reply);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendNotification(String chatId, String msg) throws TelegramApiException {
        var response = new SendMessage(chatId, msg);
        execute(response);
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            log.info("STARTED_DEMO");
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(new TelegramBot());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        };
    }
}
