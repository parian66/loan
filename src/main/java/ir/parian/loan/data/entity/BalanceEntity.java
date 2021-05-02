package ir.parian.loan.data.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
@Data
public class BalanceEntity extends AbstractPersistable<Long> {
    @OneToOne
    private MemberEntity member;

    private BigDecimal amount;
}
