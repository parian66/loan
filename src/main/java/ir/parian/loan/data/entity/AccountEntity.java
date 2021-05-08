package ir.parian.loan.data.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import java.math.BigDecimal;

import static ir.parian.loan.service.utils.BigDecimalUtil.add;
import static ir.parian.loan.service.utils.BigDecimalUtil.subtract;

@Data
@Inheritance
@Entity(name = AccountEntity.TABLE)
public abstract class AccountEntity extends AbstractPersistable<Long> {
    public static final String TABLE = "account";

    @Column(nullable = false)
    private BigDecimal balance;

    public void addBalance(BigDecimal amount) {
        balance = add(balance, amount);
    }

    public void subtractBalance(BigDecimal amount) {
        balance = subtract(balance, amount);
    }
}

