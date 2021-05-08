package ir.parian.loan.data.repository;

import ir.parian.loan.data.entity.SystemAccountEntity;
import ir.parian.loan.service.enums.SystemAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SystemAccountRepository extends JpaRepository<SystemAccountEntity, Long>, QuerydslPredicateExecutor<SystemAccountEntity> {
    SystemAccountEntity getByType(SystemAccountType type);

    Optional<SystemAccountEntity> findByType(SystemAccountType type);
}
