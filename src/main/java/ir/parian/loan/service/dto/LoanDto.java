package ir.parian.loan.service.dto;

import ir.parian.loan.service.enums.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LoanDto {
    private Long id;
    private MemberDto member;
    private Short rate;
    private BigDecimal amount;
    private LoanStatus status;
    private Integer installmentCount;
    private Date firstInstallmentDate;
    private Date nextInstallmentDate;
    private Date lastInstallmentDate;
    private String description;
}
