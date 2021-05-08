package ir.parian.loan.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import ir.parian.loan.data.entity.*;
import ir.parian.loan.data.repository.*;
import ir.parian.loan.service.AccountService;
import ir.parian.loan.service.ServiceMapper;
import ir.parian.loan.service.dto.*;
import ir.parian.loan.service.enums.SystemAccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static ir.parian.loan.service.enums.TransactionType.CREDIT;

@Service
public class AccountServiceImpl implements AccountService {
    private final ServiceMapper serviceMapper;
    private final MonthlyRepository monthlyRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MemberAccountRepository memberAccountRepository;
    private final SystemAccountRepository systemAccountRepository;

    public AccountServiceImpl(final ServiceMapper serviceMapper,
                              final MonthlyRepository monthlyRepository,
                              final MemberRepository memberRepository, final TransactionRepository transactionRepository,
                              final AccountRepository accountRepository, final MemberAccountRepository memberAccountRepository, final SystemAccountRepository systemAccountRepository) {
        this.serviceMapper = serviceMapper;
        this.monthlyRepository = monthlyRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.memberAccountRepository = memberAccountRepository;
        this.systemAccountRepository = systemAccountRepository;
    }

    @Override
    @Transactional
    public void createMonthly(final Long memberAccountId) {
        final MemberAccountEntity account = memberAccountRepository.getOne(memberAccountId);
        account.addOverdueMonthlyAmount(account.getMonthlyAmount());
        memberAccountRepository.save(account);

        final MonthlyEntity monthly = new MonthlyEntity();
        monthly.setAmount(account.getMonthlyAmount());
        monthly.setAccount(account);
        monthly.setPaid(false);
        monthly.setDate(new Date());
        monthlyRepository.save(monthly);
    }

    @Override
    @Transactional
    public void createSystemAccount(final SystemAccountType type) {
        final SystemAccountEntity entity = new SystemAccountEntity();
        entity.setType(type);
        entity.setBalance(BigDecimal.ZERO);
        systemAccountRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemAccountDto> findSystemAccount(final SystemAccountType type) {
        return systemAccountRepository.findByType(type).map(serviceMapper::toSystemAccountDto);
    }

    @Override
    @Transactional
    public MemberAccountDto createMemberAccount(final NewMemberAccountDto dto) {
        final MemberEntity member = memberRepository.getOne(dto.getMemberId());
        final MemberAccountEntity memberAccount = new MemberAccountEntity();
        memberAccount.setMember(member);
        memberAccount.setBalance(BigDecimal.ZERO);
        memberAccount.setMonthlyAmount(dto.getMonthlyAmount());
        memberAccountRepository.save(memberAccount);
        return serviceMapper.toMemberAccountDto(memberAccount);
    }

    @Override
    @Transactional
    public void payMonthly(final PayMonthlyDto dto) {
        final MonthlyEntity monthlyEntity = monthlyRepository.getByIdAndAccountId(dto.getMonthlyId(), dto.getMemberAccountId());

        if (Boolean.TRUE.equals(monthlyEntity.getPaid())) {
            return;
        }

        if (Boolean.TRUE.equals(dto.getCreateTransaction())) {
            final NewTransactionDto transactionDto = new NewTransactionDto();
            transactionDto.setType(CREDIT);
            transactionDto.setMemberAccountId(dto.getMemberAccountId());
            transactionDto.setAmount(monthlyEntity.getAmount());
            transactionDto.setDescription(dto.getDescription());
            createTransaction(transactionDto);
        }

        monthlyEntity.setPaid(true);
        monthlyRepository.save(monthlyEntity);

        final MemberAccountEntity account = monthlyEntity.getAccount();
        account.subtractOverdueMonthlyAmount(monthlyEntity.getAmount());
        accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberAccountDto> loadMemberAccounts(final Pageable pageRequest) {
        return memberAccountRepository.findAll(pageRequest)
                .map(serviceMapper::toMemberAccountDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberAccountDto> findMemberAccountById(final Long memberAccountId) {
        return memberAccountRepository.findById(memberAccountId)
                .map(serviceMapper::toMemberAccountDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyDto> loadMonthlies(final Long memberAccountId, final Pageable pageRequest) {
        final MemberAccountEntity memberAccount = memberAccountRepository.getOne(memberAccountId);
        final BooleanExpression expression = QMonthlyEntity.monthlyEntity.account.eq(memberAccount);
        return monthlyRepository.findAll(expression, pageRequest)
                .map(serviceMapper::toMonthlyDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> loadTransactions(final Long memberAccountId, final Pageable pageRequest) {
        final MemberAccountEntity memberAccount = memberAccountRepository.getOne(memberAccountId);
        final BooleanExpression expression = QTransactionEntity.transactionEntity.account.eq(memberAccount);
        return transactionRepository.findAll(expression, pageRequest)
                .map(serviceMapper::toTransactionDto);
    }

    @Override
    @Transactional
    public MemberAccountDto updateMemberAccount(final UpdateAccountDto updateAccountDto) {
        final MemberAccountEntity memberAccount = memberAccountRepository.getOne(updateAccountDto.getMemberAccountId());
        memberAccount.setMonthlyAmount(updateAccountDto.getMonthlyAmount());
        memberAccountRepository.save(memberAccount);
        return serviceMapper.toMemberAccountDto(memberAccount);
    }

    @Override
    @Transactional
    public void createTransaction(final NewTransactionDto dto) {
        final AccountEntity memberAccount;
        if (dto.getSystemAccountType() != null) {
            memberAccount = systemAccountRepository.getByType(dto.getSystemAccountType());
        } else {
            memberAccount = memberAccountRepository.getOne(dto.getMemberAccountId());
        }

        if (dto.getType() == CREDIT) {
            memberAccount.addBalance(dto.getAmount());
        } else {
            memberAccount.subtractBalance(dto.getAmount());
        }
        accountRepository.save(memberAccount);

        final TransactionEntity transaction = new TransactionEntity();
        transaction.setDate(new Date());
        transaction.setType(dto.getType());
        transaction.setAccount(memberAccount);
        transaction.setAmount(dto.getAmount());
        transaction.setBalance(memberAccount.getBalance());
        transaction.setDescription(dto.getDescription());
        transactionRepository.save(transaction);
    }
}
