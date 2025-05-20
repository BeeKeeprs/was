package kr.co.webee.presentation.profile.businesscert.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.profile.businesscert.service.BusinessCertificateService;
import kr.co.webee.presentation.annotation.UserId;
import kr.co.webee.presentation.profile.businesscert.api.BusinessCertificateApi;
import kr.co.webee.presentation.profile.businesscert.dto.request.BusinessCertificateCreateRequest;
import kr.co.webee.presentation.profile.businesscert.dto.response.BusinessCertificateCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile/business-certificate")
@RequiredArgsConstructor
public class BusinessCertificateController implements BusinessCertificateApi {
    private final BusinessCertificateService businessCertificateService;

    @Override
    @PostMapping()
    public BusinessCertificateCreateResponse createBusinessCertificate(@RequestBody @Valid BusinessCertificateCreateRequest request,
                                                                       @UserId Long userId) {
        return businessCertificateService.createBusinessCertificate(request, userId);
    }

}
