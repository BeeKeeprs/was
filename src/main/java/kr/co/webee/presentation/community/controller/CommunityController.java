package kr.co.webee.presentation.community.controller;

import kr.co.webee.application.community.service.CommunityService;
import kr.co.webee.presentation.community.api.CommunityApi;
import kr.co.webee.presentation.community.dto.response.ActiveUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
public class CommunityController implements CommunityApi {

    private final CommunityService communityService;

    @Override
    @GetMapping("/active-users")
    public List<ActiveUserResponse> getActiveUsers() {
        return communityService.getActiveUsers();
    }
}
