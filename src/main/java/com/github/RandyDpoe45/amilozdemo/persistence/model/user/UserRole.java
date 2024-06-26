package com.github.RandyDpoe45.amilozdemo.persistence.model.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.user.dtos.UserResponseViews;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(UserResponseViews.UserResponseView.class)
    private Long id;

    @JsonView(UserResponseViews.UserResponseView.class)
    private String name;

    public UserRole(String name){
        this.name = name;
    }
}
