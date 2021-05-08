package ir.parian.loan.data.entity;

import ir.parian.loan.service.enums.SystemAccountType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
public class SystemAccountEntity extends AccountEntity {
    @Enumerated(EnumType.STRING)
    private SystemAccountType type;
}
