package ir.parian.loan.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class InstallmentEntity extends AbstractAuditable<Long> {
    @ManyToOne
    private LoanEntity loan;

    private Integer number;

    private BigDecimal amount;

    private Boolean paid;

    @Temporal(TemporalType.DATE)
    private Date date;
}
