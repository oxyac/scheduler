package com.oxyac.horaire.data.entity;

import com.oxyac.horaire.telegram.ScheduleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

// http://orar.ase.md/documents/orar_ff/2022-2023/REI/iarna/Anul IV_REI_ÎFR.pdf
@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Schedule extends AbstractBaseEntity {
    @Column(name = "type", nullable = false)
    @NotNull
    private ScheduleType type;
    @Column(name = "year_range", nullable = false)
    @NotBlank
    private String yearRange;
    @Column(name = "faculty", nullable = false)
    @NotBlank
    private String faculty;
    @Column(name = "semester")
    private String semester;
    @Column(name = "filename", nullable = false)
    @NotNull
    private String filename;
    @Column(name = "link", nullable = false)
    private String link;
    @Column(name = "basename")
    private String baseName;
}
