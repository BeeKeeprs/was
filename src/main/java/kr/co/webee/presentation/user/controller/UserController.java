package kr.co.webee.presentation.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.webee.application.user.service.UserService;
import kr.co.webee.common.constant.JwtConstants;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.support.util.cookie.CookieUtil;
import kr.co.webee.presentation.user.api.UserApi;
import kr.co.webee.presentation.user.dto.response.UserProfileImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserProfileImageUploadResponse uploadProfileImage(@RequestPart("image") MultipartFile image, @UserId Long userId) {
        return userService.uploadProfileImage(userId, image);
    }

    @Override
    @DeleteMapping("/me")
    public String withdraw(@UserId Long userId, HttpServletResponse response) {
        userService.withdraw(userId);
        CookieUtil.deleteCookie(JwtConstants.REFRESH_TOKEN_COOKIE_KEY, response);
        return "OK";
    }
}
