package ir.parian.loan.web.rest.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberAccountResponse {
    private Long id;
    private MemberResponse member;
    private BigDecimal balance;
    private BigDecimal monthlyAmount;
    private BigDecimal overdueMonthlyAmount;
}
