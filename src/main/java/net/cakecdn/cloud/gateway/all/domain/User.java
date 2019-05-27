package net.cakecdn.cloud.gateway.all.domain;

import net.cakecdn.cloud.gateway.register.service.RegisterForm;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Long id;

    @Column(length = 32, unique = true, nullable = false)
    private String username;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(length = 64, unique = true, nullable = false)
    private String email;

    @Column(length = 11, unique = true, nullable = false)
    private String cellphone;

    @Column(length = 128, nullable = false)
    private String avatar;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> authorities;

    private Date created;

    private Date updated;

    private boolean disabled = false;

    public User(RegisterForm form, List<Role> roles) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.username = form.getUsername();
        this.password = encoder.encode(form.getPassword());
        this.cellphone = form.getCellphone();
        this.email = form.getEmail();
        this.avatar = "#";
        this.authorities = roles;
        this.created = new Date();
    }
}
