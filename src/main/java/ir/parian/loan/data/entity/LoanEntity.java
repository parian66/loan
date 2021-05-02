package ir.parian.loan.data.entity;

import ir.parian.loan.service.enums.LoanStatus;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class LoanEntity extends AbstractAuditable<Long> {
    @ManyToOne
    private MemberEntity member;

    @Column(nullable = false)
    private Short rate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private Integer installmentCount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date firstInstallmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date nextInstallmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastInstallmentDate;

    private String description;
}
