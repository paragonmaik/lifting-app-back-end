package com.hoister.tonshoister.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.hoister.tonshoister.models.Program;
import java.util.List;

public interface ProgramRepository extends ListCrudRepository<Program, Integer> {
  List<Program> findAllByUserId(String userId);
}
