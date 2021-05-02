package ir.parian.loan.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InstallmentDto {
    private Long id;
    private Integer number;
    private BigDecimal amount;
    private Boolean paid;
    private Date date;
}
