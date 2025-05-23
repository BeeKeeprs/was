package kr.co.webee.application.profile.businesscert.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.domain.profile.businesscert.entity.BusinessCertificate;
import kr.co.webee.domain.profile.businesscert.repository.BusinessCertificateRepository;
import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.geocoding.client.GeocodingClient;
import kr.co.webee.presentation.profile.businesscert.dto.request.BusinessCertificateCreateRequest;
import kr.co.webee.presentation.profile.businesscert.dto.response.BusinessCertificateCreateResponse;
import kr.co.webee.presentation.profile.businesscert.dto.response.BusinessCertificateDetailResponse;
import kr.co.webee.presentation.profile.businesscert.dto.response.BusinessCertificateListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BusinessCertificateService {
    private final BusinessCertificateRepository businessCertificateRepository;
    private final UserRepository userRepository;
    private final GeocodingClient geocodingClient;

    @Transactional
    public BusinessCertificateCreateResponse createBusinessCertificate(BusinessCertificateCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        //open api 사업자등록증 인증 기능 추후 구현

        //사업자등록증 이미지 첨부(선택) 기능 추후 구현

        Coordinates coordinates = geocodingClient.getCoordinateFrom(request.businessAddress());

        BusinessCertificate businessCertificate = request.toEntity(coordinates, user);
        businessCertificateRepository.save(businessCertificate);

        return BusinessCertificateCreateResponse.of(businessCertificate.getId());
    }

    @Transactional(readOnly = true)
    public List<BusinessCertificateListResponse> getBusinessCertificateList() {
        List<BusinessCertificate> businessCertificates = businessCertificateRepository.findAll();

        return businessCertificates.stream()
                .map(BusinessCertificateListResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessCertificateDetailResponse getBusinessCertificateDetail(Long businessCertificateId) {
        BusinessCertificate businessCertificate = businessCertificateRepository.findById(businessCertificateId)
                .orElseThrow(() -> new EntityNotFoundException("Business Certificate not found"));

        return BusinessCertificateDetailResponse.from(businessCertificate);
    }
}
