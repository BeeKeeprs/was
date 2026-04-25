package kr.co.webee.application.user.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.application.auth.service.RefreshTokenService;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.storage.FileStorageClient;
import kr.co.webee.presentation.user.dto.response.UserProfileImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final FileStorageClient fileStorageClient;

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> readById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public UserProfileImageUploadResponse uploadProfileImage(Long userId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        String prefix = "users/" + userId + "/profile";
        String imageUrl = fileStorageClient.upload(image, prefix);

        user.updateProfileImageUrl(imageUrl);

        return UserProfileImageUploadResponse.of(imageUrl);
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        refreshTokenService.delete(userId);
        userRepository.delete(user);
    }
}
