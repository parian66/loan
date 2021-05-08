package ir.parian.loan.service;

import ir.parian.loan.service.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<UserDto> findByUsername(String username);

    UserDto saveOrUpdate(UserDto userDto);
}
