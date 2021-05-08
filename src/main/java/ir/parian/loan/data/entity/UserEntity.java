package ir.parian.loan.data.entity;

import ir.parian.loan.service.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity(name = UserEntity.TABLE)
public class UserEntity extends AbstractAuditable<Long> {
    public static final String TABLE = "user_table";

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
