package ir.parian.loan.web.rest.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MonthlyResponse {
    private Long id;
    private BigDecimal amount;
    private Boolean paid;
    private Date date;
}
