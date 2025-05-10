package kr.co.webee.application.profile.crop.service;

import kr.co.webee.application.user.service.UserService;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.common.error.exception.NotFoundException;
import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.infrastructure.geocoding.dto.CoordinatesDto;
import kr.co.webee.infrastructure.geocoding.service.GeocodingService;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropRequest;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropDetailResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCropManagementService {
    private final UserCropService userCropService;
    private final UserService userService;
    private final GeocodingService geocodingService;

    public void addUserCrop(UserCropRequest request, Long userId) {
        User user = userService.readById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));
        CoordinatesDto coordinatesDto = geocodingService.searchCoordinatesFrom(request.cultivationRegion());
        UserCrop userCrop = request.toEntity(coordinatesDto, user);

        userCropService.save(userCrop);
    }

    public List<UserCropListResponse> getUserCropList(Long userId) {
        List<UserCrop> userCrops = userCropService.readByUserId(userId);

        return userCrops.stream()
                .map(UserCropListResponse::from)
                .toList();
    }

    public UserCropDetailResponse getUserCropDetail(Long userCropId) {
        UserCrop userCrop = userCropService.readById(userCropId)
                .orElseThrow(() -> new NotFoundException(UserCrop.class, userCropId));

        return UserCropDetailResponse.from(userCrop);
    }

    public void updateUserCrop(Long userCropId, UserCropRequest request, Long userId) {
        UserCrop userCrop = userCropService.readById(userCropId)
                .orElseThrow(() -> new NotFoundException(UserCrop.class, userCropId));

        validateCropOwner(userId, userCrop);

        if (userCrop.isNotSameCultivationRegion(request.cultivationRegion())) {
            CoordinatesDto coordinatesDto = geocodingService.searchCoordinatesFrom(request.cultivationRegion());

            userCrop.updateRegionInfo(
                    request.cultivationRegion(),
                    coordinatesDto.latitude(),
                    coordinatesDto.longitude()
            );
        }

        userCrop.updateCultivationInfo(
                request.name(),
                request.variety(),
                request.cultivationType(),
                request.cultivationArea(),
                request.plantingDate()
        );
    }

    public void deleteUserCrop(Long userCropId, Long userId) {
        UserCrop userCrop = userCropService.readById(userCropId)
                .orElseThrow(() -> new NotFoundException(UserCrop.class, userCropId));

        validateCropOwner(userId, userCrop);

        userCropService.delete(userCropId);
    }

    private static void validateCropOwner(Long userId, UserCrop userCrop) {
        if (userCrop.isNotOwnedBy(userId)) {
            throw new BusinessException(ErrorType.ACCESS_DENIED);
        }
    }
}
