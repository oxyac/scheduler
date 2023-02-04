package com.oxyac.horaire.data.repo;

import com.oxyac.horaire.data.entity.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Person findById(long id);

    Optional<Person> getByChatId(long chatId);
}
