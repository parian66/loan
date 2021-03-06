package ir.parian.loan.data.repository;

import ir.parian.loan.data.entity.InstallmentEntity;
import ir.parian.loan.data.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface InstallmentRepository extends JpaRepository<InstallmentEntity, Long>, QuerydslPredicateExecutor<InstallmentEntity> {
    List<InstallmentEntity> findByLoanOrderByNumber(LoanEntity loan);
}
