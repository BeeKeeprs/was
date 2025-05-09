package kr.co.webee.application.profile.crop.service;

import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.profile.crop.repository.UserCropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCropService {
    private final UserCropRepository userCropRepository;

    public void save(UserCrop userCrop) {
        userCropRepository.save(userCrop);
    }

    public Optional<UserCrop> readById(Long id) {
        return userCropRepository.findById(id);
    }

    public List<UserCrop> readByUserId(Long userId) {
        return userCropRepository.findByUserId(userId);
    }

}
