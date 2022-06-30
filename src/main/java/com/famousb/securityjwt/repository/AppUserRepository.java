package com.famousb.securityjwt.repository;

import com.famousb.securityjwt.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);

    @Query("select c from AppUser c where (c.email=?1 OR c.username=?1) ")
    Optional<AppUser> findByUsernameOrEmail(String query);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
