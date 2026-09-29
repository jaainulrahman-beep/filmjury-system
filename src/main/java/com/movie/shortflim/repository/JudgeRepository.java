package com.movie.shortflim.repository;

import java.util.List;
import java.util.Optional;

import com.movie.shortflim.entity.Judge;

public interface JudgeRepository {

    Judge save(Judge judge);

    List<Judge> findAll();

    Optional<Judge> findById(Long id);

    long count();
}
