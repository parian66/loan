package ir.parian.loan.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import ir.parian.loan.data.entity.InstallmentEntity;
import ir.parian.loan.data.entity.LoanEntity;
import ir.parian.loan.data.entity.MemberEntity;
import ir.parian.loan.data.entity.QLoanEntity;
import ir.parian.loan.data.repository.InstallmentRepository;
import ir.parian.loan.data.repository.LoanRepository;
import ir.parian.loan.data.repository.MemberRepository;
import ir.parian.loan.service.AccountService;
import ir.parian.loan.service.LoanService;
import ir.parian.loan.service.ServiceMapper;
import ir.parian.loan.service.dto.*;
import ir.parian.loan.service.enums.LoanStatus;
import ir.parian.loan.service.enums.SystemAccountType;
import ir.parian.loan.service.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.querydsl.core.types.dsl.Expressions.allOf;
import static ir.parian.loan.service.impl.ExpressionHelper.safeExpression;
import static ir.parian.loan.service.utils.BigDecimalUtil.divide;
import static ir.parian.loan.service.utils.CalendarUtil.addPersianMonth;
import static ir.parian.loan.service.utils.CalendarUtil.isPast;

@Service
public class LoanServiceImpl implements LoanService {
    private final ServiceMapper serviceMapper;
    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final InstallmentRepository installmentRepository;
    private final AccountService accountService;

    public LoanServiceImpl(final ServiceMapper serviceMapper,
                           final LoanRepository loanRepository,
                           final MemberRepository memberRepository,
                           final InstallmentRepository installmentRepository,
                           final AccountService accountService) {
        this.serviceMapper = serviceMapper;
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.installmentRepository = installmentRepository;
        this.accountService = accountService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanDto> search(final LoanFilterDto loanFilterDto, final Pageable pageRequest) {
        final BooleanExpression expression = safeExpression(loanFilterDto, filter -> allOf(
                safeExpression(filter.getMemberId(), QLoanEntity.loanEntity.member.id::eq),
                safeExpression(filter.getStatus(), QLoanEntity.loanEntity.status::eq)));
        return loanRepository.findAll(expression, pageRequest).map(serviceMapper::toLoanDto);
    }

    @Override
    @Transactional
    public LoanDto createLoan(final NewLoanDto newLoanDto) {
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

        final NewTransactionDto newTransactionDto = new NewTransactionDto();
        newTransactionDto.setType(TransactionType.CREDIT);
        newTransactionDto.setSystemAccountType(SystemAccountType.INCOME);
        newTransactionDto.setAmount(divide(loan.getAmount(), loan.getRate()));
        newTransactionDto.setDescription("Loan#" + loan.getId());
        accountService.createTransaction(newTransactionDto);

        return serviceMapper.toLoanDto(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanDto> findByLoanId(final Long loanId) {
        return loanRepository.findById(loanId).map(serviceMapper::toLoanDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstallmentDto> findInstallmentsByLoadId(final Long loanId) {
        final LoanEntity loan = loanRepository.getOne(loanId);
        final List<InstallmentEntity> installmentEntities = installmentRepository.findByLoanOrderByNumber(loan);
        return serviceMapper.toInstallmentDtos(installmentEntities);
    }

    @Override
    @Transactional
    public void repay(final RepayLoadDto dto) {
        final LoanEntity loan = loanRepository.getOne(dto.getId());

        final List<InstallmentEntity> installmentList = installmentRepository.findByLoanOrderByNumber(loan);
        final List<InstallmentEntity> repayList = installmentList.stream()
                .filter(i -> !i.getPaid())
                .limit(dto.getCount())
                .collect(Collectors.toList());
        repayList.forEach(p -> p.setPaid(true));
        installmentRepository.saveAll(repayList);

        final Optional<InstallmentEntity> mayNextInstallment = installmentList.stream().filter(i -> !i.getPaid()).findFirst();
        if (mayNextInstallment.isPresent()) {
            final InstallmentEntity nextInstallment = mayNextInstallment.get();
            loan.setNextInstallmentDate(nextInstallment.getDate());
            loan.setStatus(isPast(nextInstallment.getDate()) ? LoanStatus.OVERDUE : LoanStatus.OK);
        } else {
            loan.setStatus(LoanStatus.DONE);
            loan.setNextInstallmentDate(null);
        }
        loanRepository.save(loan);

        if (dto.getMemberAccountId() != null) {
            final BigDecimal amount = repayList.stream().map(InstallmentEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            final NewTransactionDto newTransactionDto = new NewTransactionDto();
            newTransactionDto.setType(TransactionType.DEBIT);
            newTransactionDto.setDescription(dto.getDescription());
            newTransactionDto.setMemberAccountId(dto.getMemberAccountId());
            newTransactionDto.setAmount(amount);
            accountService.createTransaction(newTransactionDto);
        }
    }

    private Function<Integer, InstallmentEntity> createInstallment(final LoanEntity loan) {
        final BigDecimal count = BigDecimal.valueOf(loan.getInstallmentCount());
        final BigDecimal amount = loan.getAmount().divide(count, RoundingMode.FLOOR);
        final BigDecimal carry = loan.getAmount().subtract(count.multiply(count));

        return (number) -> {
            final InstallmentEntity installment = new InstallmentEntity();
            installment.setLoan(loan);
            installment.setPaid(false);
            installment.setNumber(number);
            installment.setAmount(amount);
            installment.setDate(addPersianMonth(loan.getFirstInstallmentDate(), number - 1));

            if (number.equals(loan.getInstallmentCount())) {
                installment.setAmount(amount.add(carry));
            }

            return installment;
        };
    }
}
