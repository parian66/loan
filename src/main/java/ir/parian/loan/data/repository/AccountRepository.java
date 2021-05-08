package ir.parian.loan.data.repository;

import ir.parian.loan.data.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>, QuerydslPredicateExecutor<AccountEntity> {
}
