package ir.parian.loan.service;

import ir.parian.loan.service.dto.*;
import ir.parian.loan.service.enums.SystemAccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountService {
    Optional<SystemAccountDto> findSystemAccount(SystemAccountType type);

    void createSystemAccount(SystemAccountType type);

    Page<MemberAccountDto> loadMemberAccounts(Pageable pageRequest);

    Optional<MemberAccountDto> findMemberAccountById(Long memberAccountId);

    MemberAccountDto createMemberAccount(NewMemberAccountDto newMemberAccountDto);

    MemberAccountDto updateMemberAccount(UpdateAccountDto updateAccountDto);

    Page<TransactionDto> loadTransactions(Long memberAccountId, final Pageable pageRequest);

    void createTransaction(NewTransactionDto depositDto);

    Page<MonthlyDto> loadMonthlies(Long memberAccountId, final Pageable pageRequest);

    void createMonthly(final Long memberAccountId);

    void payMonthly(PayMonthlyDto payMonthlyDto);
}
