package kr.co.webee.application.profile.crop.service;

import kr.co.webee.application.user.service.UserService;
import kr.co.webee.common.error.exception.NotFoundException;
import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.infrastructure.geocoding.dto.CoordinatesDto;
import kr.co.webee.infrastructure.geocoding.service.GeocodingService;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCropManagementService {
    private final UserCropService userCropService;
    private final UserService userService;
    private final GeocodingService geocodingService;

    public void addUserCrop(UserCropRequest request, Long userId) {
        User user = userService.readById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        CoordinatesDto coordinatesDto = geocodingService.searchCoordinatesFrom(request.cultivationRegion());
        UserCrop userCrop = request.toEntity(coordinatesDto, user);

        userCropService.save(userCrop);
    }
}
