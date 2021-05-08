package ir.parian.loan.data.repository;

import ir.parian.loan.data.entity.MemberAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberAccountRepository extends JpaRepository<MemberAccountEntity, Long>, QuerydslPredicateExecutor<MemberAccountEntity> {
}
