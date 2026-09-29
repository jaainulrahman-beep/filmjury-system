package com.movie.shortflim.repository;

import java.util.List;
import java.util.Optional;

import com.movie.shortflim.entity.FilmEntry;

public interface FilmEntryRepository {

    FilmEntry save(FilmEntry entry);

    List<FilmEntry> findAll();

    Optional<FilmEntry> findById(Long id);

    long count();
}
