package ir.parian.loan.service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountDto {
    private Long memberAccountId;
    private BigDecimal monthlyAmount;
}
