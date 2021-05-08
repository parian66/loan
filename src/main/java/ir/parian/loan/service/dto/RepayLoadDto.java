package ir.parian.loan.service.dto;

import lombok.Data;

@Data
public class RepayLoadDto {
    private Long id;
    private Short count;
    private Long memberAccountId;
    private String description;
}
