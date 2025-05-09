package kr.co.webee.presentation.profile.crop.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.profile.crop.service.UserCropManagementService;
import kr.co.webee.presentation.annotation.UserId;
import kr.co.webee.presentation.profile.crop.api.UserCropApi;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropRequest;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles/crops")
public class UserCropController implements UserCropApi {
    private final UserCropManagementService userCropManagementService;

    @Override
    @PostMapping("")
    public void addUserCrop(@RequestBody @Valid UserCropRequest request, @UserId Long userId) {
        userCropManagementService.addUserCrop(request, userId);
    }

    @GetMapping("")
    public List<UserCropListResponse> getUserCropList(@UserId Long userId) {
        return userCropManagementService.getUserCropList(userId);
    }

}
