package com.oxyac.horaire.data.repo;

import com.oxyac.horaire.data.entity.Search;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SearchRepository extends CrudRepository<Search, Integer> {

    Search findFirstByChatIdOrderByIdDesc(Long chatId);

    Optional<Search> findTopByChatId(Long chatId);
}
