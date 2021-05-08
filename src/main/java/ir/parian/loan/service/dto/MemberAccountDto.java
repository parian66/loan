package ir.parian.loan.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberAccountDto {
    private Long id;
    private MemberDto member;
    private BigDecimal balance;
    private BigDecimal monthlyAmount;
    private BigDecimal overdueMonthlyAmount;
}
