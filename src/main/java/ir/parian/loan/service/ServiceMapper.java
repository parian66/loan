package ir.parian.loan.service;

import ir.parian.loan.data.entity.*;
import ir.parian.loan.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    MemberDto toMemberDto(MemberEntity memberEntity);

    MemberEntity toMemberEntity(MemberDto memberDto);

    void fillMemberEntity(MemberDto memberDto, @MappingTarget MemberEntity memberEntity);

    LoanDto toLoanDto(LoanEntity loanEntity);

    LoanEntity toLoanEntity(NewLoanDto newLoanDto);

    InstallmentDto toInstallmentDto(InstallmentEntity installmentEntity);

    List<InstallmentDto> toInstallmentDtos(List<InstallmentEntity> installmentEntities);

    MemberAccountDto toMemberAccountDto(MemberAccountEntity memberAccountEntity);

    TransactionDto toTransactionDto(TransactionEntity transactionEntity);

    MonthlyDto toMonthlyDto(MonthlyEntity monthlyEntity);

    UserDto toUserDto(UserEntity userEntity);

    void fillUserEntity(UserDto userDto, @MappingTarget UserEntity entity);

    SystemAccountDto toSystemAccountDto(SystemAccountEntity systemAccountEntity);
}
