package kr.co.webee.presentation.profile.business.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.profile.business.service.BusinessService;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.profile.business.api.BusinessApi;
import kr.co.webee.presentation.profile.business.dto.request.BusinessCreateRequest;
import kr.co.webee.presentation.profile.business.dto.response.BusinessCreateResponse;
import kr.co.webee.presentation.profile.business.dto.response.BusinessDetailResponse;
import kr.co.webee.presentation.profile.business.dto.response.BusinessListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile/business")
@RequiredArgsConstructor
public class BusinessController implements BusinessApi {
    private final BusinessService businessService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BusinessCreateResponse createBusiness(
            @RequestPart("request") @Valid BusinessCreateRequest request,
            @RequestPart(value = "businessCertificateImage", required = false) MultipartFile image,
            @UserId Long userId) {
        return businessService.createBusiness(request, image, userId);
    }

    @Override
    @GetMapping()
    public List<BusinessListResponse> getBusinessList() {
        return businessService.getBusinessList();
    }

    @Override
    @GetMapping("/me")
    public List<BusinessListResponse> getBusinessListByMe(@UserId Long userId) {
        return businessService.getBusinessListByMe(userId);
    }

    @Override
    @GetMapping("/{businessId:[0-9]+}")
    public BusinessDetailResponse getBusinessDetail(@PathVariable Long businessId) {
        return businessService.getBusinessDetail(businessId);
    }

    @Override
    @GetMapping("/test")
    public Long test(@UserId Long userId) {
        return userId;
    }
}
