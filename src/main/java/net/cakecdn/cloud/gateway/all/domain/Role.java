package net.cakecdn.cloud.gateway.all.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private long id;

    @Column(nullable = false, length = 32)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
