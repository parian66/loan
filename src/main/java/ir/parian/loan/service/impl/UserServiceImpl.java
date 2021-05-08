package ir.parian.loan.service.impl;

import ir.parian.loan.data.entity.UserEntity;
import ir.parian.loan.data.repository.UserRepository;
import ir.parian.loan.service.ServiceMapper;
import ir.parian.loan.service.UserService;
import ir.parian.loan.service.dto.UserDto;
import ir.parian.loan.web.security.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ServiceMapper serviceMapper;

    public UserServiceImpl(final UserRepository userRepository, final ServiceMapper serviceMapper) {
        this.userRepository = userRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByUsername(final String username) {
        return userRepository.findByUsername(username).map(serviceMapper::toUserDto);
    }

    @Override
    @Transactional
    public UserDto saveOrUpdate(final UserDto userDto) {
        final UserEntity entity;
        if (userDto.getId() == null) {
            entity = new UserEntity();
        } else {
            entity = userRepository.getOne(userDto.getId());
        }
        serviceMapper.fillUserEntity(userDto, entity);
        userRepository.save(entity);
        return serviceMapper.toUserDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        final UserDto user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        final List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .authorities(authorities)
                .build();
    }
}
