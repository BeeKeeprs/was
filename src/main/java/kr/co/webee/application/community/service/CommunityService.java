package kr.co.webee.application.community.service;

import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.community.dto.response.ActiveUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ActiveUserResponse> getActiveUsers() {
        LocalDateTime since = LocalDateTime.now().minusHours(1);

        return userRepository.findByLastActivityAtAfter(since).stream()
                .map(ActiveUserResponse::from)
                .toList();
    }
}
