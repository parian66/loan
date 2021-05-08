package ir.parian.loan.service.dto;

import ir.parian.loan.service.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean enabled;
    private Set<Role> roles;
}
