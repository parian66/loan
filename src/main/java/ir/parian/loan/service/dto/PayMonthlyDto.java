package ir.parian.loan.service.dto;

import lombok.Data;

@Data
public class PayMonthlyDto {
    Long memberAccountId;
    Long monthlyId;
    Boolean createTransaction;
    String description;
}
