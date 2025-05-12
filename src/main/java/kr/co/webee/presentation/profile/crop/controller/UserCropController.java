package kr.co.webee.presentation.profile.crop.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.profile.crop.service.UserCropService;
import kr.co.webee.presentation.annotation.UserId;
import kr.co.webee.presentation.profile.crop.api.UserCropApi;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropRequest;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropDetailResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/crops")
public class UserCropController implements UserCropApi {
    private final UserCropService userCropService;

    @Override
    @PostMapping("")
    public Map<String, Long> createUserCrop(@RequestBody @Valid UserCropRequest request, @UserId Long userId) {
        return userCropService.createUserCrop(request, userId);
    }

    @Override
    @GetMapping("")
    public List<UserCropListResponse> getUserCropList(@UserId Long userId) {
        return userCropService.getUserCropList(userId);
    }

    @Override
    @GetMapping("/{userCropId}")
    public UserCropDetailResponse getUserCropDetail(@PathVariable Long userCropId) {
        return userCropService.getUserCropDetail(userCropId);
    }

    @PutMapping("/{userCropId}")
    @Override
    public void updateUserCrop(@PathVariable Long userCropId, @RequestBody @Valid UserCropRequest request, @UserId Long userId) {
        userCropService.updateUserCrop(userCropId, request, userId);
    }

    @Override
    @DeleteMapping("/{userCropId}")
    public void deleteUserCrop(@PathVariable Long userCropId, @UserId Long userId) {
        userCropService.deleteUserCrop(userCropId, userId);
    }
}
