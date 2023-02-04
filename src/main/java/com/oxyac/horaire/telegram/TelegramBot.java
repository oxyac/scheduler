package com.oxyac.horaire.telegram;

import com.oxyac.horaire.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.io.Serializable;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot implements LongPollingBot {

    private final AppProperties properties;

    private final UpdateReceiver updateReceiver;

    public TelegramBot(AppProperties properties, UpdateReceiver updateReceiver) {
        this.properties = properties;
        this.updateReceiver = updateReceiver;
    }

    @Override
    public void onRegister() {
        log.info(this.getBotUsername());
    }

    public String getBotUsername() {
        return properties.getName();
    }

    public String getBotToken() {
        return properties.getToken();
    }

    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
            try {
                execute(answerCallbackQuery);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }

        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);

        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {
                    executeWithExceptionCheck((SendMessage) response);
                } else if (response instanceof AnswerInlineQuery) {
                    sendApiMethod((AnswerInlineQuery) response);
                }
            });
        }
    }

    public void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("oops");
        }
    }

    public void sendApiMethod(AnswerInlineQuery sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
