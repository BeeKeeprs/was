package kr.co.webee.application.profile.crop.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.domain.profile.crop.entity.Location;
import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.profile.crop.repository.UserCropRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.geocoding.service.GeocodingService;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropRequest;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropDetailResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCropService {
    private final UserCropRepository userCropRepository;
    private final GeocodingService geocodingService;
    private final UserRepository userRepository;

    @Transactional
    public Map<String,Long> createUserCrop(UserCropRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Coordinates coordinates = geocodingService.searchCoordinatesFrom(request.cultivationAddress());

        UserCrop userCrop = request.toEntity(coordinates, user);
        userCropRepository.save(userCrop);

        return Map.of("userCropId",userCrop.getId());
    }

    @Transactional(readOnly = true)
    public List<UserCropListResponse> getUserCropList(Long userId) {
        List<UserCrop> userCrops = userCropRepository.findByUserId(userId);

        return userCrops.stream()
                .map(UserCropListResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserCropDetailResponse getUserCropDetail(Long userCropId) {
        UserCrop userCrop = userCropRepository.findById(userCropId)
                .orElseThrow(() -> new EntityNotFoundException("User Crop not found"));

        return UserCropDetailResponse.from(userCrop);
    }

    @Transactional
    public void updateUserCrop(Long userCropId, UserCropRequest request, Long userId) {
        UserCrop userCrop = userCropRepository.findById(userCropId)
                .orElseThrow(() -> new EntityNotFoundException("User Crop not found"));

        validateCropOwner(userId, userCrop);

        if (userCrop.isNotSameCultivationAddress(request.cultivationAddress())) {
            Coordinates coordinates = geocodingService.searchCoordinatesFrom(request.cultivationAddress());

            Location cultivationLocation = Location.builder()
                    .address(request.cultivationAddress())
                    .coordinates(coordinates)
                    .build();

            userCrop.updateCultivationLocation(cultivationLocation);
        }

        userCrop.update(request.name(), request.variety(), request.cultivationType(), request.cultivationArea(), request.plantingDate());
    }

    @Transactional
    public void deleteUserCrop(Long userCropId, Long userId) {
        UserCrop userCrop = userCropRepository.findById(userCropId)
                .orElseThrow(() -> new EntityNotFoundException("User Crop not found"));

        validateCropOwner(userId, userCrop);

        userCropRepository.deleteById(userCropId);
    }

    private static void validateCropOwner(Long userId, UserCrop userCrop) {
        if (userCrop.isNotOwnedBy(userId)) {
            throw new BusinessException(ErrorType.ACCESS_DENIED);
        }
    }
}
