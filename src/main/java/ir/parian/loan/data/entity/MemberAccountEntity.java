package ir.parian.loan.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

import static ir.parian.loan.service.utils.BigDecimalUtil.add;
import static ir.parian.loan.service.utils.BigDecimalUtil.subtract;

@Data
@Entity
public class MemberAccountEntity extends AccountEntity {
    @ManyToOne
    private MemberEntity member;

    @Column(nullable = false)
    private BigDecimal monthlyAmount;

    private BigDecimal overdueMonthlyAmount;

    public void addOverdueMonthlyAmount(BigDecimal amount) {
        overdueMonthlyAmount = add(overdueMonthlyAmount, amount);
    }

    public void subtractOverdueMonthlyAmount(BigDecimal amount) {
        overdueMonthlyAmount = subtract(overdueMonthlyAmount, amount);
    }
}
