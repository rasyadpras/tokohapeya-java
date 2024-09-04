package com.enigma.challenge.tokohapeya.repository;

import com.enigma.challenge.tokohapeya.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, String> {
}
