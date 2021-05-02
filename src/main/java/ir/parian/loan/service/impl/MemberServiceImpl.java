package ir.parian.loan.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import ir.parian.loan.data.entity.MemberEntity;
import ir.parian.loan.data.entity.QMemberEntity;
import ir.parian.loan.data.repository.MemberRepository;
import ir.parian.loan.service.MemberService;
import ir.parian.loan.service.ServiceMapper;
import ir.parian.loan.service.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

import static com.querydsl.core.types.dsl.Expressions.anyOf;
import static ir.parian.loan.service.ExpressionHelper.cleanSearch;
import static ir.parian.loan.service.ExpressionHelper.safeExpression;

@Service
public class MemberServiceImpl implements MemberService {
    private final ServiceMapper serviceMapper;
    private final MemberRepository memberRepository;

    public MemberServiceImpl(final ServiceMapper serviceMapper,
                             final MemberRepository memberRepository) {
        this.serviceMapper = serviceMapper;
        this.memberRepository = memberRepository;
    }

    @Override
    public Page<MemberDto> search(final String search, final Pageable pageRequest) {
        final Predicate expression = safeExpression(cleanSearch(search), toSearchPredicate());
        return memberRepository.findAll(expression, pageRequest).map(serviceMapper::toMemberDto);
    }

    @Override
    public Optional<MemberDto> findById(final Long memberId) {
        return memberRepository.findById(memberId)
                .map(serviceMapper::toMemberDto);
    }

    @Override
    public void saveOrUpdate(final MemberDto memberDto) {
        final MemberEntity memberEntity;
        if (memberDto.getId() == null) {
            memberEntity = serviceMapper.toMemberEntity(memberDto);
        } else {
            memberEntity = memberRepository.getOne(memberDto.getId());
            serviceMapper.fillMemberEntity(memberDto, memberEntity);
        }
        memberRepository.save(memberEntity);
    }

    @Override
    public void deleteById(final Long memberId) {
        memberRepository.deleteById(memberId);
    }

    private Function<String, BooleanExpression> toSearchPredicate() {
        return (search) -> {
            final String term = "%" + search + "%";
            return anyOf(
                    QMemberEntity.memberEntity.firstName.likeIgnoreCase(term),
                    QMemberEntity.memberEntity.lastName.likeIgnoreCase(term),
                    QMemberEntity.memberEntity.nationalCode.likeIgnoreCase(term),
                    QMemberEntity.memberEntity.phoneNumber.likeIgnoreCase(term)
            );
        };
    }
}
