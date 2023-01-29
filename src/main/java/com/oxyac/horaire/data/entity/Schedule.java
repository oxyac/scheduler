package com.oxyac.horaire.data.entity;

import com.oxyac.horaire.telegram.ScheduleType;
import com.oxyac.horaire.telegram.State;
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

// http://orar.ase.md/documents/orar_ff/2022-2023/REI/iarna/Anul IV_REI_ÎFR.pdf
@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends AbstractBaseEntity {
    @Column(name = "type", nullable = false)
    @NotNull
    private ScheduleType type;

    @Column(name = "year_range", nullable = false)
    @NotBlank
    private String yearRange;

    @Column(name = "filename", nullable = false)
    @NotNull
    private Integer filename;

    @Column(name = "basename", nullable = false)
    @NotNull
    private Integer baseName;

    @Column(name = "faculty", nullable = false)
    @NotBlank
    private String faculty;
}
