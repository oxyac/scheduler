package com.oxyac.horaire.data.repo;

import com.oxyac.horaire.data.entity.Person;
import com.oxyac.horaire.data.entity.Schedule;
import com.oxyac.horaire.telegram.ScheduleType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    @Query("select distinct h.type from Schedule h")
    List<ScheduleType> findDistinctType();
    List<Schedule> findByType(ScheduleType type);

}
