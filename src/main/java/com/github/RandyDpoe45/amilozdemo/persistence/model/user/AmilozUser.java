package com.github.RandyDpoe45.amilozdemo.persistence.model.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.user.dtos.UserResponseViews;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@Accessors(chain = true)
public class AmilozUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(UserResponseViews.UserResponseView.class)
    private Long id;

    @OneToOne(targetEntity = AuthenticationUser.class, fetch = FetchType.LAZY)
    private AuthenticationUser authenticationUser;

    @Column(unique = true)
    @JsonView(UserResponseViews.UserResponseView.class)
    private String email;

    @JsonView(UserResponseViews.UserResponseView.class)
    private String name;

    @JsonView(UserResponseViews.UserResponseView.class)
    private String lastname;

    @JsonView(UserResponseViews.UserResponseView.class)
    private String documentId;

}
