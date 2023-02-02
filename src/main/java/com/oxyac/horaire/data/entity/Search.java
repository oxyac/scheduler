package com.oxyac.horaire.data.entity;

import com.oxyac.horaire.telegram.ScheduleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "searches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Search extends AbstractBaseEntity {
    @Column(name = "chat_id", nullable = false)
    @NotNull
    private Long chatId;

    @Column(name = "search_state", nullable = false)
    private SearchState searchState;
    @Column(name = "type")
    private ScheduleType type;

    @Column(name = "year_range")
    private String yearRange;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "semester")
    private String semester;

    @Column(name= "search_datetime")
    @CreationTimestamp
    private LocalDateTime searchDateTime;

    public Search(Long chatId) {
        this.chatId = chatId;
    }
}
