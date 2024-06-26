package com.github.RandyDpoe45.amilozdemo.config.startup;

import com.github.RandyDpoe45.amilozdemo.persistence.enums.InterestTimeSpanEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.InterestTypeEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.PaymentTypeEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.UserRoleEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.InterestTimeSpanJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.InterestTypeJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.PaymentTypeJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.AmilozUserJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.AuthenticationUserJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.UserRoleJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.InterestTimeSpan;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.InterestType;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.PaymentType;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AmilozUser;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StartUpConfig {

    private final UserRoleJpaRepository userRoleJpaRepository;

    private final PaymentTypeJpaRepository paymentTypeJpaRepository;

    private final InterestTypeJpaRepository interestTypeJpaRepository;

    private final AuthenticationUserJpaRepository authenticationUserJpaRepository;

    private final PasswordEncoder passwordEncoder;

    private final AmilozUserJpaRepository amilozUserJpaRepository;

    private final ConfigProperties configProperties;
    private final InterestTimeSpanJpaRepository interestTimeSpanJpaRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void startUpConfig() {
        createUserRoles();
        createAdminUser();
        createPaymentTypes();
        createInterestTypes();
        createInterestTimeSpans();
    }

    private void createInterestTimeSpans(){
        List<InterestTimeSpan> currentInterestTypes = interestTimeSpanJpaRepository.findAll();
        List<String> interestTimeSpanNames = currentInterestTypes.stream().map(InterestTimeSpan::getCode).toList();
        List<InterestTimeSpanEnum> missingTypes = Arrays.stream(InterestTimeSpanEnum.values())
                .filter(x -> !interestTimeSpanNames.contains(x.getCode()))
                .toList();
        missingTypes.forEach(x -> interestTimeSpanJpaRepository.save(new InterestTimeSpan().setCode(x.getCode())));
    }

    private void createInterestTypes(){
        List<InterestType> currentInterestTypes = interestTypeJpaRepository.findAll();
        List<String> interestTypesNames = currentInterestTypes.stream().map(InterestType::getCode).toList();
        List<InterestTypeEnum> missingTypes = Arrays.stream(InterestTypeEnum.values())
                .filter(x -> !interestTypesNames.contains(x.getCode()))
                .toList();
        missingTypes.forEach(x -> interestTypeJpaRepository.save(new InterestType().setCode(x.getCode())));
    }

    private void createPaymentTypes(){
        List<PaymentType> currentInterestTypes = paymentTypeJpaRepository.findAll();
        List<String> paymentTypesNames = currentInterestTypes.stream().map(PaymentType::getCode).toList();
        List<PaymentTypeEnum> missingTypes = Arrays.stream(PaymentTypeEnum.values())
                .filter(x -> !paymentTypesNames.contains(x.getCode()))
                .toList();
        missingTypes.forEach(x -> paymentTypeJpaRepository.save(new PaymentType().setCode(x.getCode())));
    }

    private void createUserRoles() {
        List<UserRole> currentUserRoles = userRoleJpaRepository.findAll();
        List<String> roleNames = currentUserRoles.stream().map(UserRole::getName).toList();
        List<UserRoleEnum> missingRoles = Arrays.stream(UserRoleEnum.values())
                .filter(x -> !roleNames.contains(x.getCode()))
                .toList();
        missingRoles.forEach(x -> userRoleJpaRepository.save(new UserRole(x.getCode())));
    }

    private void createAdminUser() {
        UserRole adminRole = userRoleJpaRepository.findByName(
                UserRoleEnum.ADMIN_ROlE.getCode()
        );
        AuthenticationUser authUser = authenticationUserJpaRepository.findByUsername(configProperties.getAdminUserConfig().getUsername());
        if (Objects.isNull(authUser)) {
            AuthenticationUser adminUSer = new AuthenticationUser()
                    .setUsername(configProperties.getAdminUserConfig().getUsername())
                    .setEnabled(true)
                    .setUserRoles(List.of(adminRole))
                    .setPassword(passwordEncoder.encode(configProperties.getAdminUserConfig().getPassword()));
            authenticationUserJpaRepository.saveAndFlush(adminUSer);
            AmilozUser amilozUser = new AmilozUser()
                    .setAuthenticationUser(adminUSer);
            amilozUserJpaRepository.saveAndFlush(amilozUser);
        }
    }
}
