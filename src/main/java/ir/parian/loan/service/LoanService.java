package ir.parian.loan.service;

import ir.parian.loan.service.dto.InstallmentDto;
import ir.parian.loan.service.dto.LoanDto;
import ir.parian.loan.service.dto.LoanFilterDto;
import ir.parian.loan.service.dto.NewLoanDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface LoanService {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Page<LoanDto> search(final LoanFilterDto loanFilterDto, final Pageable pageRequest);

    @Transactional
    LoanDto create(final NewLoanDto newLoanDto);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<LoanDto> findByLoanId(final Long loanId);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    List<InstallmentDto> findInstallmentsByLoadId(final Long loanId);

    @Transactional
    void repay(final Long loanId, final int count);
}
