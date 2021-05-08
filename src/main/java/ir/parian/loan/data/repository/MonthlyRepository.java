package ir.parian.loan.data.repository;

import ir.parian.loan.data.entity.MemberAccountEntity;
import ir.parian.loan.data.entity.MonthlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface MonthlyRepository extends JpaRepository<MonthlyEntity, Long>, QuerydslPredicateExecutor<MonthlyEntity> {
    List<MonthlyEntity> findByAccountAndPaidFalseOrderByDate(MemberAccountEntity account);

    Optional<MonthlyEntity> findFirstByAccountAndPaidTrueOrderByDateDesc(MemberAccountEntity account);

    MonthlyEntity getByIdAndAccountId(Long id, Long accountId);
}
