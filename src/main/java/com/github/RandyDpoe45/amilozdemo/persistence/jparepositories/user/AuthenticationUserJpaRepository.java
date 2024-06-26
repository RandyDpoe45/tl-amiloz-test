package com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user;

import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationUserJpaRepository extends JpaRepository<AuthenticationUser, Long> {

    AuthenticationUser findByUsername(String username);
}
