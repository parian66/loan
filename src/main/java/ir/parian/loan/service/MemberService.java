package ir.parian.loan.service;

import ir.parian.loan.service.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public interface MemberService {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Page<MemberDto> search(final String search, final Pageable pageRequest);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<MemberDto> findById(final Long memberId);

    @Transactional
    void saveOrUpdate(final MemberDto memberDto);

    @Transactional
    void deleteById(Long memberId);
}
