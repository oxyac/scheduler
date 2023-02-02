package com.oxyac.horaire.telegram;

import com.oxyac.horaire.data.entity.Person;
import com.oxyac.horaire.data.entity.SearchState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.oxyac.horaire.data.entity.SearchState.QUERY_FREQUENCY;

public class TelegramUtil {
    public static SendMessage createMessageTemplate(Person user) {
        return createMessageTemplate(String.valueOf(user.getChatId()));
    }

    // Создаем шаблон SendMessage с включенным Markdown
    public static SendMessage createMessageTemplate(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableMarkdown(true);
        return message;
    }

    // Создаем кнопку
    public static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(command);
        return inlineKeyboardButton;
    }

    public static InlineKeyboardMarkup createInlineKeyboard(List<String> data, SearchState callbackMoment) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        data.removeAll(Collections.singleton(null));
        data.sort(String::compareTo);
        int idx = 0;
        for (String s : data) {
            idx++;
            keyboardButtons.add(createInlineKeyboardButton(s, callbackMoment + "|" + s));
            if (idx % 3 == 0) {
                keyboard.add(keyboardButtons);
                keyboardButtons = new ArrayList<>();
            }
        }
        keyboard.add(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
}
