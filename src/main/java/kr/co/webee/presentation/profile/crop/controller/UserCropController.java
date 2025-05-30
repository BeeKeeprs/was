package kr.co.webee.presentation.profile.crop.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.profile.crop.service.UserCropService;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.profile.crop.api.UserCropApi;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropCreateRequest;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropUpdateRequest;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropAddressListResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropCreateResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropDetailResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/crops")
public class UserCropController implements UserCropApi {
    private final UserCropService userCropService;

    @Override
    @PostMapping("")
    public UserCropCreateResponse createUserCrop(@RequestBody @Valid UserCropCreateRequest request, @UserId Long userId) {
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

    @Override
    @GetMapping("/addresses")
    public UserCropAddressListResponse getUserCropAddressList(@UserId Long userId) {
        return userCropService.getUserCropAddressList(userId);
    }

    @PutMapping("/{userCropId}")
    @Override
    public void updateUserCrop(@PathVariable Long userCropId, @RequestBody @Valid UserCropUpdateRequest request, @UserId Long userId) {
        userCropService.updateUserCrop(userCropId, request, userId);
    }

    @Override
    @DeleteMapping("/{userCropId}")
    public void deleteUserCrop(@PathVariable Long userCropId, @UserId Long userId) {
        userCropService.deleteUserCrop(userCropId, userId);
    }
}
