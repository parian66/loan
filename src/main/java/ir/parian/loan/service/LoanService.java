package ir.parian.loan.service;

import ir.parian.loan.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    Page<LoanDto> search(final LoanFilterDto loanFilterDto, final Pageable pageRequest);

    LoanDto createLoan(final NewLoanDto newLoanDto);

    Optional<LoanDto> findByLoanId(final Long loanId);

    List<InstallmentDto> findInstallmentsByLoadId(final Long loanId);

    void repay(final RepayLoadDto repayLoadDto);
}
