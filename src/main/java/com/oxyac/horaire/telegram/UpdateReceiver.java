package com.oxyac.horaire.telegram;
import com.oxyac.horaire.data.entity.Person;
import com.oxyac.horaire.data.repo.PersonRepository;
import com.oxyac.horaire.telegram.handler.Handler;
import com.oxyac.horaire.data.entity.State;
import com.oxyac.horaire.telegram.handler.InlineHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class UpdateReceiver {
    // Храним доступные хендлеры в списке (подсмотрел у Miroha)
    private final List<Handler> handlers;
    // Имеем доступ в базу пользователей
    private final PersonRepository personRepository;

    private InlineHandler inlineHandler;

    public UpdateReceiver(List<Handler> handlers, PersonRepository userRepository, InlineHandler inlineHandler) {
        this.handlers = handlers;
        this.personRepository = userRepository;
        this.inlineHandler = inlineHandler;
    }

    // Обрабатываем полученный Update
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                final Long chatId = message.getFrom().getId();
                final User userFrom = message.getFrom();
                final Person user = this.personRepository.getByChatId(chatId)
                        .orElseGet(() -> this.personRepository.save(new Person(chatId, userFrom)));
                return getHandlerByState(user.getBotState()).handle(user, message.getText());

            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                final Long chatId = callbackQuery.getFrom().getId();
                final Person user = personRepository.getByChatId(chatId)
                        .orElseGet(() -> personRepository.save(new Person(chatId)));

                return getHandlerByCallBackQuery(callbackQuery.getData()).handle(user, callbackQuery.getData());
            } else if (update.hasInlineQuery()) {
                final InlineQuery inlineQuery = update.getInlineQuery();
                final Long chatId = inlineQuery.getFrom().getId();
                final Person user = personRepository.getByChatId(chatId)
                        .orElseGet(() -> personRepository.save(new Person(chatId)));

                return inlineHandler.handle(user, inlineQuery);
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            log.error("No action found");
            log.info(update.toString());
            return Collections.emptyList();
        }
    }

    public Handler getHandlerByState(State state) {
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        log.info(query);
        return handlers.stream()
                .filter(h -> {
                    log.info(h.operatedCallBackQuery().toString());
                    return h.operatedCallBackQuery().stream()
                            .anyMatch(query::startsWith);
                })
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
