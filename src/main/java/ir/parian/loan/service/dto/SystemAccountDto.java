package ir.parian.loan.service.dto;

import ir.parian.loan.service.enums.SystemAccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SystemAccountDto {
    private SystemAccountType type;
    private BigDecimal balance;
}
