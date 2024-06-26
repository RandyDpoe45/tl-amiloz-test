package com.github.RandyDpoe45.amilozdemo.config.startup;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AdminUserConfigProperties {
    private String username;
    private String password;
}
