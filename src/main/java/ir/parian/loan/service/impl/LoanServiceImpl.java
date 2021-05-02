package ir.parian.loan.service.impl;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import com.querydsl.core.types.dsl.BooleanExpression;
import ir.parian.loan.data.entity.InstallmentEntity;
import ir.parian.loan.data.entity.LoanEntity;
import ir.parian.loan.data.entity.MemberEntity;
import ir.parian.loan.data.entity.QLoanEntity;
import ir.parian.loan.data.repository.InstallmentRepository;
import ir.parian.loan.data.repository.LoanRepository;
import ir.parian.loan.data.repository.MemberRepository;
import ir.parian.loan.service.LoanService;
import ir.parian.loan.service.ServiceMapper;
import ir.parian.loan.service.dto.InstallmentDto;
import ir.parian.loan.service.dto.LoanDto;
import ir.parian.loan.service.dto.LoanFilterDto;
import ir.parian.loan.service.dto.NewLoanDto;
import ir.parian.loan.service.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.querydsl.core.types.dsl.Expressions.allOf;
import static ir.parian.loan.service.ExpressionHelper.safeExpression;

@Service
public class LoanServiceImpl implements LoanService {
    private final ServiceMapper serviceMapper;
    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final InstallmentRepository installmentRepository;

    public LoanServiceImpl(final ServiceMapper serviceMapper,
                           final LoanRepository loanRepository,
                           final MemberRepository memberRepository,
                           final InstallmentRepository installmentRepository) {
        this.serviceMapper = serviceMapper;
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.installmentRepository = installmentRepository;
    }

    @Override
    public Page<LoanDto> search(final LoanFilterDto loanFilterDto, final Pageable pageRequest) {
        final BooleanExpression expression = safeExpression(loanFilterDto, filter -> allOf(
                safeExpression(filter.getMemberId(), QLoanEntity.loanEntity.member.id::eq),
                safeExpression(filter.getStatus(), QLoanEntity.loanEntity.status::eq)));
        return loanRepository.findAll(expression, pageRequest).map(serviceMapper::toLoanDto);
    }

    @Override
    public LoanDto create(final NewLoanDto newLoanDto) {
        final MemberEntity member = memberRepository.getOne(newLoanDto.getMemberId());
        final LoanEntity loan = serviceMapper.toLoanEntity(newLoanDto);
        loan.setMember(member);
        loan.setStatus(LoanStatus.NEW);
        loan.setNextInstallmentDate(loan.getFirstInstallmentDate());

        final List<InstallmentEntity> installments = IntStream.rangeClosed(1, newLoanDto.getInstallmentCount())
                .boxed()
                .map(createInstallment(loan))
                .collect(Collectors.toList());
        loan.setLastInstallmentDate(installments.get(installments.size() - 1).getDate());

        loanRepository.save(loan);
        installmentRepository.saveAll(installments);
        return serviceMapper.toLoanDto(loan);
    }

    @Override
    public Optional<LoanDto> findByLoanId(final Long loanId) {
        return loanRepository.findById(loanId).map(serviceMapper::toLoanDto);
    }

    @Override
    public List<InstallmentDto> findInstallmentsByLoadId(final Long loanId) {
        final List<InstallmentEntity> installmentEntities = installmentRepository.findByLoanId(loanId);
        return serviceMapper.toInstallmentDtos(installmentEntities);
    }

    @Override
    public void repay(final Long loanId, final int count) {
        final LoanEntity loan = loanRepository.getOne(loanId);

        final List<InstallmentEntity> installmentList = installmentRepository.findByLoanId(loanId).stream()
                .sorted(Comparator.comparingInt(InstallmentEntity::getNumber))
                .collect(Collectors.toList());

        final List<InstallmentEntity> repayList = installmentList.stream()
                .filter(i -> !i.getPaid())
                .limit(count)
                .collect(Collectors.toList());
        repayList.forEach(p -> p.setPaid(true));

        final Optional<InstallmentEntity> mayNextInstallment = installmentList.stream().filter(i -> !i.getPaid()).findFirst();
        if (mayNextInstallment.isPresent()) {
            final InstallmentEntity nextInstallment = mayNextInstallment.get();
            loan.setNextInstallmentDate(nextInstallment.getDate());
            loan.setStatus(getToday().after(nextInstallment.getDate()) ? LoanStatus.OVERDUE : LoanStatus.OK);
        } else {
            loan.setStatus(LoanStatus.DONE);
            loan.setNextInstallmentDate(loan.getLastInstallmentDate());
        }

        loanRepository.save(loan);
        installmentRepository.saveAll(repayList);
    }

    private Function<Integer, InstallmentEntity> createInstallment(final LoanEntity loan) {
        final BigDecimal count = BigDecimal.valueOf(loan.getInstallmentCount());
        final BigDecimal amount = loan.getAmount().divide(count, RoundingMode.FLOOR);
        final BigDecimal carry = loan.getAmount().subtract(count.multiply(count));

        return (number) -> {
            final Calendar date = getToday();
            date.setTime(loan.getFirstInstallmentDate());
            date.add(Calendar.MONTH, number - 1);

            final InstallmentEntity installment = new InstallmentEntity();
            installment.setNumber(number);
            installment.setAmount(amount);
            installment.setPaid(false);
            installment.setDate(date.getTime());
            installment.setLoan(loan);

            if (number.equals(loan.getInstallmentCount())) {
                installment.setAmount(amount.add(carry));
            }

            return installment;
        };
    }

    private Calendar getToday() {
        return Calendar.getInstance(ULocale.forLanguageTag("fa"));
    }
}
