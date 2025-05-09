package kr.co.webee.application.profile.crop.service;

import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.profile.crop.repository.UserCropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCropService {
    private final UserCropRepository userCropRepository;

    public void save(UserCrop userCrop) {
        userCropRepository.save(userCrop);
    }

    public List<UserCrop> readByUserId(Long userId) {
        return userCropRepository.findByUserId(userId);
    }
}
