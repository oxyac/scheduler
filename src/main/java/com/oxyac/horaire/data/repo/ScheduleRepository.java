package com.oxyac.horaire.data.repo;

import com.oxyac.horaire.data.entity.Person;
import com.oxyac.horaire.data.entity.Schedule;
import com.oxyac.horaire.telegram.ScheduleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    @Query("select distinct h.type from Schedule h")
    ArrayList<String> findDistinctType();

    @Query("select distinct h.yearRange from Schedule h where h.type = ?1")
    List<String> findDistinctYearRangeByType(ScheduleType type);

    @Query("select distinct h.semester from Schedule h where h.type = ?1 and h.yearRange = ?2")
    List<String> findDistinctSemesterByFilter(ScheduleType type, String yearRange);

    @Query("select distinct h.faculty from Schedule h where h.type = ?1 and h.yearRange = ?2 and h.semester = ?3")
    List<String> findDistinctFacultyByFilter(ScheduleType type, String yearRange, String semester);
    @Query("select h from Schedule h where h.type = ?1 and h.yearRange = ?2 and h.semester = ?3 and h.faculty = ?4")
    List<Schedule> findScheduleBySearch(@NotNull ScheduleType type, @NotBlank String yearRange, String semester, @NotBlank String faculty);

}
