package com.oxyac.horaire.data.repo;

import com.oxyac.horaire.data.entity.Search;
import com.oxyac.horaire.telegram.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SearchRepository extends CrudRepository<Search, Integer> {

}
