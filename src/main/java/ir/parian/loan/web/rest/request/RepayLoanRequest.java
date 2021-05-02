package ir.parian.loan.web.rest.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RepayLoanRequest {
    @NotNull
    @Min(1)
    @Max(24)
    private Short count;
}
