package com.oxyac.horaire.telegram.handler;

import com.oxyac.horaire.data.entity.*;
import com.oxyac.horaire.data.repo.PersonRepository;
import com.oxyac.horaire.data.repo.ScheduleRepository;
import com.oxyac.horaire.data.repo.SearchRepository;
import com.oxyac.horaire.telegram.ScheduleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.oxyac.horaire.data.entity.SearchState.*;
import static com.oxyac.horaire.telegram.TelegramUtil.*;

@Slf4j
@Component
public class SearchHandler implements Handler {
    private final PersonRepository personRepository;
    private final SearchRepository searchRepository;

    private final ScheduleRepository scheduleRepository;
    private final StartHandler startHandler;

    private Person person;
    private String message;

    public SearchHandler(ScheduleRepository scheduleRepository, SearchRepository searchRepository,
                         PersonRepository personRepository, StartHandler startHandler) {
        this.scheduleRepository = scheduleRepository;
        this.searchRepository = searchRepository;
        this.personRepository = personRepository;
        this.startHandler = startHandler;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Person person, String message) {
        log.info(message);
        Search search = searchRepository.findTopByChatId(person.getChatId());
        this.message = message;
        this.person = person;
        if (message.startsWith(QUERY_FREQUENCY.toString())) {
            SendMessage sendMessage = saveTypeQueryYears(search);
            return List.of(sendMessage);
        } else if (message.startsWith(QUERY_YEAR.toString())) {
            SendMessage sendMessage = saveYearsQuerySemester(search);
            return List.of(sendMessage);
        } else if (message.startsWith(QUERY_SEMESTER.toString())) {
            SendMessage sendMessage = saveSemesterQueryFaculty(search);
            return List.of(sendMessage);
        } else if (message.startsWith(QUERY_FACULTY.toString())) {
            return saveFacultyReturnSchedule(search);
        }
        person.setBotState(State.START);
        personRepository.save(person);
        return startHandler.handle(person, message);
    }

    private List<PartialBotApiMethod<? extends Serializable>> saveFacultyReturnSchedule(Search search) {
        search.setFaculty(message.split("\\|")[1]);
        search.setSearchState(DOWNLOAD_FILE);
        searchRepository.save(search);

        SendMessage sendMessage = createMessageTemplate(person);
        List<Schedule> scheduleList = scheduleRepository.findScheduleBySearch(search.getType(), search.getYearRange(), search.getSemester(), search.getFaculty());

        sendMessage.setText("Schedules for this semester are: ");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        int idx = 0;
        for (Schedule s : scheduleList) {
            idx++;
            InlineKeyboardButton button = createInlineKeyboardButton(s.getBaseName(), DOWNLOAD_FILE + " " + s.getId());
            button.setUrl(s.getLink());
            rowInline.add(button);
            if (idx % 3 == 0) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
            }
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        return List.of(sendMessage);
    }

    private SendMessage saveSemesterQueryFaculty(Search search) {
        search.setSemester(message.split("\\|")[1]);
        search.setSearchState(QUERY_FACULTY);
        searchRepository.save(search);

        SendMessage sendMessage = createMessageTemplate(person);
        sendMessage.setText("Please select faculty:");
        List<String> scheduleList = scheduleRepository.findDistinctFacultyByFilter(search.getType(), search.getYearRange(), search.getSemester());
        sendMessage.setReplyMarkup(createInlineKeyboard(scheduleList, QUERY_FACULTY));
        return sendMessage;
    }

    private SendMessage saveYearsQuerySemester(Search search) {
        search.setYearRange(message.split("\\|")[1]);
        search.setSearchState(QUERY_SEMESTER);
        searchRepository.save(search);

        SendMessage sendMessage = createMessageTemplate(person);
        sendMessage.setText("Please select semester:");
        List<String> scheduleList = scheduleRepository.findDistinctSemesterByFilter(search.getType(), search.getYearRange());
        sendMessage.setReplyMarkup(createInlineKeyboard(scheduleList, QUERY_SEMESTER));
        return sendMessage;
    }

    private SendMessage saveTypeQueryYears(Search search) {
        search.setType(ScheduleType.valueOf(message.split("\\|")[1]));
        search.setSearchState(QUERY_YEAR);
        searchRepository.save(search);

        SendMessage sendMessage = createMessageTemplate(person);
        sendMessage.setText("Please select year of studies");
        List<String> scheduleList = scheduleRepository.findDistinctYearRangeByType(search.getType());
        sendMessage.setReplyMarkup(createInlineKeyboard(scheduleList, QUERY_YEAR));
        return sendMessage;
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
