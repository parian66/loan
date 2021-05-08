package ir.parian.loan.web.rest.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class NewMemberAccountRequest {
    @NotNull
    private Long memberId;

    @DecimalMin("1000")
    private BigDecimal monthlyAmount;
}
