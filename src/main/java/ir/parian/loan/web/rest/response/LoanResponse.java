package ir.parian.loan.web.rest.response;

import ir.parian.loan.service.enums.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class LoanResponse {
    private Long id;
    private MemberResponse member;
    private Short rate;
    private BigDecimal amount;
    private LoanStatus status;
    private Integer installmentCount;
    private Date firstInstallmentDate;
    private Date nextInstallmentDate;
    private Date lastInstallmentDate;
    private String description;
    private List<InstallmentResponse> installments;
}
