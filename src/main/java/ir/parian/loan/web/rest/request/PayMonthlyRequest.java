package ir.parian.loan.web.rest.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PayMonthlyRequest {
    @NotNull
    Boolean createTransaction;

    String description;
}
