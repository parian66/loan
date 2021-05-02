package ir.parian.loan.data.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class UserEntity extends AbstractPersistable<Long> {
    public enum Role {
        ADMIN, USER;
    }

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @Column(nullable = false)
    private Boolean enabled;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
