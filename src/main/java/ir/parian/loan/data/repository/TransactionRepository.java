package ir.parian.loan.data.repository;

import ir.parian.loan.data.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>, QuerydslPredicateExecutor<TransactionEntity> {
}
