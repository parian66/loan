package ir.parian.loan.web.rest.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InstallmentResponse {
    private Long id;
    private Integer number;
    private BigDecimal amount;
    private Boolean paid;
    private Date date;
}
