package kr.co.webee.presentation.inquiry.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.inquiry.service.InquiryService;
import kr.co.webee.presentation.inquiry.api.InquiryApi;
import kr.co.webee.presentation.inquiry.dto.request.InquirySubmitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
@RestController
public class InquiryController implements InquiryApi {
    private final InquiryService inquiryService;

    @Override
    @PostMapping
    public String submitInquiry(@RequestBody @Valid InquirySubmitRequest request) {
        inquiryService.submitInquiry(request);
        return "OK";
    }
}
