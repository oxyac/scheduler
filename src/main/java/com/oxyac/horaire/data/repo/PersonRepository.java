package com.oxyac.horaire.data.repo;

import java.util.List;
import java.util.Optional;

import com.oxyac.horaire.data.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Person findById(long id);

    Optional<Person> getByChatId(long chatId);
}
