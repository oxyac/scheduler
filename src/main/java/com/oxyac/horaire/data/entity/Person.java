package com.oxyac.horaire.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.User;

@Entity
@Table(name = "persons", uniqueConstraints = {@UniqueConstraint(columnNames = "chat_id", name = "users_unique_chatid_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person extends AbstractBaseEntity {
    @Column(name = "chat_id", unique = true, nullable = false)
    @NotNull
    private Long chatId;

    @Column(name = "name", unique = true)
    @NotBlank
    private String name;

    @Column(name = "bot_state", nullable = false)
    private State botState;
    @Column(name = "first_name")

    private String firstName;
    @Column(name = "last_name")

    private String lastName;
    @Column(name = "username")

    private String username;

    // Конструктор нужен для создания нового пользователя (а может и нет? :))
    public Person(Long chatId) {
        this.chatId = chatId;
        this.name = String.valueOf(chatId);
        this.botState = State.START;
    }

    public Person(Long chatId, User user) {
        this.chatId = chatId;
        this.name = String.valueOf(chatId);
        this.botState = State.START;
        this.username = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}
