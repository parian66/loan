package ir.parian.loan.service.dto;

import ir.parian.loan.service.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanFilterDto {
    private Long memberId;
    private LoanStatus status;
}
