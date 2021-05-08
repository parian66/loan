package ir.parian.loan.service;

import ir.parian.loan.service.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberService {
    Page<MemberDto> search(final String search, final Pageable pageRequest);

    Optional<MemberDto> findById(final Long memberId);

    void saveOrUpdate(final MemberDto memberDto);

    void deleteById(Long memberId);
}
