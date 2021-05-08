package ir.parian.loan.data.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = MemberEntity.TABLE)
public class MemberEntity extends AbstractPersistable<Long> {
    public static final String TABLE = "member";

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String nationalCode;

    @Column(nullable = false)
    private String phoneNumber;
}
