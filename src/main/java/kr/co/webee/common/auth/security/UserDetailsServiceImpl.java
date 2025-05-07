package kr.co.webee.common.auth.security;

import kr.co.webee.application.user.service.UserService;
import kr.co.webee.common.error.exception.NotFoundException;
import kr.co.webee.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userService.readById(Long.parseLong(userId))
                .map(CustomUserDetails::new)
                .orElseThrow(()-> new NotFoundException(User.class, userId));
    }
}