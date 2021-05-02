package ir.parian.loan.data.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class MemberEntity extends AbstractPersistable<Long> {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String nationalCode;

    @Column(nullable = false)
    private String phoneNumber;
}
