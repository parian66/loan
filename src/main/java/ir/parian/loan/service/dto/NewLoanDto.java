package ir.parian.loan.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NewLoanDto {
    private Long memberId;
    private Short rate;
    private BigDecimal amount;
    private Short installmentCount;
    private Date firstInstallmentDate;
    private String description;
}
