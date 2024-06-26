package com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user;

import com.github.RandyDpoe45.amilozdemo.persistence.model.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRole, Long> {

    UserRole findByName(String roleName);
}
