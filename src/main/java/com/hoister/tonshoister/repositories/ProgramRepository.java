package com.hoister.tonshoister.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.hoister.tonshoister.models.Program;

public interface ProgramRepository extends ListCrudRepository<Program, Integer> {
}
