package kr.co.webee.presentation.fcmtoken.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.fcmtoken.service.FcmTokenService;
import kr.co.webee.presentation.fcmtoken.api.FcmTokenApi;
import kr.co.webee.presentation.fcmtoken.dto.request.FcmTokenRegisterRequest;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/fcm-tokens")
@RequiredArgsConstructor
@RestController
public class FcmTokenController implements FcmTokenApi {
    private final FcmTokenService fcmTokenService;

    @Override
    @PostMapping
    public String registerToken(
            @UserId Long userId,
            @RequestBody @Valid FcmTokenRegisterRequest request
    ) {
        fcmTokenService.registerToken(userId, request);
        return "OK";
    }

    @Override
    @DeleteMapping
    public String deleteToken(
            @UserId Long userId,
            @RequestParam String deviceInfo
    ) {
        fcmTokenService.deleteToken(userId, deviceInfo);
        return "OK";
    }
}
