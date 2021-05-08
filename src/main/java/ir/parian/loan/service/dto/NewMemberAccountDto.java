package ir.parian.loan.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NewMemberAccountDto {
    private Long memberId;
    private BigDecimal monthlyAmount;
}
