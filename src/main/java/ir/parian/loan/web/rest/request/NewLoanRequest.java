package ir.parian.loan.web.rest.request;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class NewLoanRequest {
    @NotNull
    private Long memberId;

    @NotNull
    @Min(0)
    @Max(10)
    private Short rate;

    @NotNull
    @Min(1000)
    private BigDecimal amount;

    @NotNull
    @Min(1)
    @Max(24)
    private Short installmentCount;

    @NotNull
    @Future
    private Date firstInstallmentDate;

    private String description;
}
