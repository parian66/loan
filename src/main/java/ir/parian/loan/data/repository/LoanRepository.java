package ir.parian.loan.data.repository;

import ir.parian.loan.data.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LoanRepository extends JpaRepository<LoanEntity, Long>, QuerydslPredicateExecutor<LoanEntity> {
}
