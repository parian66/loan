package ir.parian.loan.service;

import ir.parian.loan.data.entity.InstallmentEntity;
import ir.parian.loan.data.entity.LoanEntity;
import ir.parian.loan.data.entity.MemberEntity;
import ir.parian.loan.service.dto.InstallmentDto;
import ir.parian.loan.service.dto.LoanDto;
import ir.parian.loan.service.dto.MemberDto;
import ir.parian.loan.service.dto.NewLoanDto;
import org.mapstruct.Mapper;
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
}
