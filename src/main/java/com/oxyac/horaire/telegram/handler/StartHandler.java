package com.oxyac.horaire.telegram.handler;

import com.oxyac.horaire.data.entity.*;
import com.oxyac.horaire.data.repo.PersonRepository;
import com.oxyac.horaire.data.repo.ScheduleRepository;
import com.oxyac.horaire.data.repo.SearchRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.oxyac.horaire.data.entity.SearchState.QUERY_FREQUENCY;
import static com.oxyac.horaire.telegram.TelegramUtil.*;

@Component
public class StartHandler implements Handler {
    @Value("${bot.name}")
    private String botUsername;

    private final PersonRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SearchRepository searchRepository;

    public StartHandler(PersonRepository userRepository,
                        ScheduleRepository scheduleRepository,
                        SearchRepository searchRepository) {
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Person person, String message) {
        // Приветствуем пользователя

        SendMessage welcomeMessage = createMessageTemplate(person);
        welcomeMessage.setText(String.format("Salut student of ASEM! I'm *%s*%nI am here to help you find your lectures schedule", botUsername));
        SendMessage registrationMessage = createMessageTemplate(person);
        registrationMessage.setText("In order to find your lectures schedule, first select frequency");
        person.setBotState(State.SEARCH_SCHEDULE);
        userRepository.save(person);

        Search search = searchRepository.findTopByChatId(person.getChatId()).orElse(new Search(person.getChatId()));
        search.setSemester(null);
        search.setFaculty(null);
        search.setYearRange(null);
        search.setType(null);
        search.setSearchState(QUERY_FREQUENCY);
        searchRepository.save(search);

        List<String> schedules = scheduleRepository.findDistinctType();
        registrationMessage.setReplyMarkup(createInlineKeyboard(schedules, QUERY_FREQUENCY));
        return List.of(welcomeMessage, registrationMessage);
    }

    @Override
    public State operatedBotState() {
        return State.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
