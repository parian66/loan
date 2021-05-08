package ir.parian.loan.web.rest;

import ir.parian.loan.service.dto.*;
import ir.parian.loan.web.rest.request.*;
import ir.parian.loan.web.rest.response.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WebMapper {
    MemberResponse toMemberResponse(MemberDto memberDto);

    MemberDto toMemberDto(MemberRequest memberRequest);

    MemberDto toMemberDto(Long id, MemberRequest memberRequest);

    InstallmentResponse toInstallmentResponse(InstallmentDto installmentDto);

    LoanResponse toLoanResponse(LoanDto loanDto);

    LoanResponse toLoanResponse(LoanDto loanDto, List<InstallmentDto> installments);

    NewLoanDto toNewLoanDto(NewLoanRequest newLoanRequest);

    RepayLoadDto toRepayLoanDto(RepayLoanRequest request, final Long id);

    MemberAccountResponse toMemberAccountResponse(MemberAccountDto memberAccountDto);

    NewMemberAccountDto toNewMemberAccountDto(NewMemberAccountRequest request);

    TransactionResponse toTransactionResponse(TransactionDto transactionDto);

    NewTransactionDto toNewTransactionDto(NewTransactionRequest request, Long memberAccountId);

    MonthlyResponse toMonthlyResponse(MonthlyDto monthlyDto);

    UpdateAccountDto toUpdateAccountDto(final Long memberAccountId, UpdateAccountRequest request);

    PayMonthlyDto toPayMonthlyDto(Long memberAccountId, Long monthlyId, PayMonthlyRequest request);
}
