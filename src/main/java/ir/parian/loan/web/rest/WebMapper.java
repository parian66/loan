package ir.parian.loan.web.rest;

import ir.parian.loan.service.dto.InstallmentDto;
import ir.parian.loan.service.dto.LoanDto;
import ir.parian.loan.service.dto.MemberDto;
import ir.parian.loan.service.dto.NewLoanDto;
import ir.parian.loan.web.rest.request.MemberRequest;
import ir.parian.loan.web.rest.request.NewLoanRequest;
import ir.parian.loan.web.rest.response.InstallmentResponse;
import ir.parian.loan.web.rest.response.LoanResponse;
import ir.parian.loan.web.rest.response.MemberResponse;
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
}
