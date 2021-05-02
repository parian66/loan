package ir.parian.loan.web.rest.response;

import lombok.Data;

@Data
public class MemberResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String phoneNumber;
}
