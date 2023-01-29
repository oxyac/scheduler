package com.oxyac.horaire.telegram.handler;

import com.oxyac.horaire.data.entity.Person;
import com.oxyac.horaire.data.entity.State;
import com.oxyac.horaire.data.repo.ScheduleRepository;
import com.oxyac.horaire.telegram.ScheduleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

import static com.oxyac.horaire.data.entity.SearchState.*;

@Slf4j
@Component
public class SearchHandler implements Handler{

    private ScheduleRepository scheduleRepository;

    public SearchHandler(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Person person, String message) {
//        if (message.startsWith(QUERY_FREQUENCY.toString())) {
//            scheduleRepository.findByType(ScheduleType.valueOf(message.split(" ")[1]));
//        } else if (message.startsWith(QUIZ_INCORRECT)) {
//            return incorrectAnswer(user);
//        } else {
//            return startNewQuiz(user);
//        }
//        return null;
        return null;
    }

    @Override
    public State operatedBotState() {
        return State.SEARCH_SCHEDULE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(QUERY_FREQUENCY.toString(), QUERY_FACULTY.toString(), QUERY_SEMESTER.toString(), QUERY_YEAR.toString());
    }
}
