package com.github.RandyDpoe45.amilozdemo.config.startup;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "startup-config")
public class ConfigProperties {

    private AdminUserConfigProperties adminUserConfig;

}
