package com.github.RandyDpoe45.amilozdemo.services.impl.user;

import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.AmilozServiceException;
import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.ExceptionCodesEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.UserRoleEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.AmilozUserJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.AuthenticationUserJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.UserRoleJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AmilozUser;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.UserRole;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.user.dtos.UserCreationDto;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.user.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationUserJpaRepository authenticationUserJpaRepository;

    private final AmilozUserJpaRepository amilozUserJpaRepository;

    private final UserRoleJpaRepository roleJpaRepository;

    private final PasswordEncoder passwordEncoder;

    private final EntityManager em;

    @Override
    @Transactional
    public AuthenticationUser createUser(UserCreationDto user) {
        UserRole userRole = roleJpaRepository.findByName(UserRoleEnum.CLIENT.getCode());
        return createUserAux(user, List.of(userRole));
    }

    @Override
    @Transactional
    public AuthenticationUser createAdminUser(UserCreationDto user) {
        UserRole userRole = roleJpaRepository.findByName(UserRoleEnum.ADMIN_ROlE.getCode());
        return createUserAux(user, List.of(userRole));
    }

    private AuthenticationUser createUserAux(
            UserCreationDto user,
            List<UserRole> userRoles
    ){
        AuthenticationUser authenticationUser = authenticationUserJpaRepository.findByUsername(user.username());
        if(!Objects.isNull(authenticationUser))
            throw new AmilozServiceException(ExceptionCodesEnum.USERNAME_IN_USE);
        AmilozUser amilozUser = amilozUserJpaRepository.findByEmail(user.email());
        if(!Objects.isNull(amilozUser))
            throw new AmilozServiceException(ExceptionCodesEnum.EMAIl_IN_USE);
        authenticationUser = new AuthenticationUser()
                .setUserRoles(userRoles)
                .setEnabled(true)
                .setUsername(user.username())
                .setPassword(passwordEncoder.encode(user.password()));
        authenticationUser = authenticationUserJpaRepository.saveAndFlush(authenticationUser);
        amilozUser = new AmilozUser()
                .setName(user.name())
                .setEmail(user.email())
                .setLastname(user.lastname())
                .setDocumentId(user.documentId())
                .setAuthenticationUser(authenticationUser);
        amilozUserJpaRepository.saveAndFlush(amilozUser);
        em.refresh(authenticationUser);
        return authenticationUser;
    }
}
