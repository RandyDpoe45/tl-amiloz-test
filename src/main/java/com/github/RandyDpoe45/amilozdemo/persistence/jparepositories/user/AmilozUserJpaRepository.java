package com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user;

import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AmilozUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmilozUserJpaRepository extends JpaRepository<AmilozUser, Long> {

    AmilozUser findByEmail(String email);
}
