package com.oxyac.horaire.telegram.handler;

import com.oxyac.horaire.data.entity.Person;
import com.oxyac.horaire.data.entity.Schedule;
import com.oxyac.horaire.data.repo.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultDocument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.oxyac.horaire.telegram.ScheduleType.orar_ff;
import static com.oxyac.horaire.telegram.ScheduleType.orar_zi;

@Slf4j
@Component
public class InlineHandler {

    private final ScheduleRepository scheduleRepository;

    public InlineHandler(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<PartialBotApiMethod<? extends Serializable>> handle(Person user, InlineQuery data) {
        List<Schedule> scheduleList;
        if (data.getQuery().trim().isEmpty()) {
            scheduleList = scheduleRepository.getListForInlineQuery("2022-2023", List.of("semestrul 2", "iarna"));
        } else if (data.getQuery().toUpperCase().trim().contains("R")) {
            scheduleList = scheduleRepository.getListForInlineQueryType("2022-2023", List.of("semestrul 2", "iarna"), orar_ff);
        } else {
            scheduleList = scheduleRepository.getListForInlineQueryType("2022-2023", List.of("semestrul 2", "iarna"), orar_zi);

        }
        List<InlineQueryResult> results = new ArrayList<>();

        for (int i = 0; i < 50 && i < scheduleList.size(); i++) {
            InlineQueryResultDocument result = new InlineQueryResultDocument();
            result.setDocumentUrl(scheduleList.get(i).getLink().replaceAll(" ", "%20"));
            result.setTitle(scheduleList.get(i).getYearRange() + " - " + scheduleList.get(i).getFaculty() + " - " + scheduleList.get(i).getBaseName());
            result.setId(scheduleList.get(i).getId().toString());
            result.setMimeType("application/pdf");

            results.add(result);
        }
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(data.getId());
        answerInlineQuery.setResults(results);
        return List.of(answerInlineQuery);
    }
}
