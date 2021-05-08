package ir.parian.loan.web.rest.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class UpdateAccountRequest {
    @DecimalMin("1")
    private BigDecimal monthlyAmount;
}
