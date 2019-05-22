package net.cakecdn.cloud.gateway.all.config.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "cake.db")
public class CakeDbProperty {
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
}
