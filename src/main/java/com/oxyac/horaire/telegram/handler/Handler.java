package com.oxyac.horaire.telegram.handler;

import com.oxyac.horaire.data.entity.Person;
import com.oxyac.horaire.data.entity.State;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;


public interface Handler {

    // основной метод, который будет обрабатывать действия пользователя
    List<PartialBotApiMethod<? extends Serializable>> handle(Person person, String message);

    // метод, который позволяет узнать, можем ли мы обработать текущий State у пользователя
    State operatedBotState();

    // метод, который позволяет узнать, какие команды CallBackQuery мы можем обработать в этом классе
    List<String> operatedCallBackQuery();
}
