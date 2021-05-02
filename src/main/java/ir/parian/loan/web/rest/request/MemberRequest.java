package ir.parian.loan.web.rest.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Data
public class MemberRequest {
    @NotBlank
    @Length(min = 3)
    private String firstName;

    @NotBlank
    @Length(min = 3)
    private String lastName;

    @NotBlank
    @Digits(integer = 10, fraction = 0)
    private String nationalCode;

    private String phoneNumber;
}
