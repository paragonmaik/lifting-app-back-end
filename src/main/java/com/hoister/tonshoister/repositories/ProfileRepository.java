package com.hoister.tonshoister.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.hoister.tonshoister.models.Profile;

public interface ProfileRepository extends ListCrudRepository<Profile, String> {
}
