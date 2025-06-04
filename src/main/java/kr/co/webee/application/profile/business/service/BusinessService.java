package kr.co.webee.application.profile.business.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.domain.profile.business.entity.Business;
import kr.co.webee.domain.profile.business.repository.BusinessRepository;
import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.geocoding.client.GeocodingClient;
import kr.co.webee.infrastructure.storage.FileStorageClient;
import kr.co.webee.presentation.profile.business.dto.request.BusinessCreateRequest;
import kr.co.webee.presentation.profile.business.dto.response.BusinessCreateResponse;
import kr.co.webee.presentation.profile.business.dto.response.BusinessDetailResponse;
import kr.co.webee.presentation.profile.business.dto.response.BusinessListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final GeocodingClient geocodingClient;
    private final FileStorageClient fileStorageClient;

    @Transactional
    public BusinessCreateResponse createBusiness(BusinessCreateRequest request, MultipartFile file, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        //open api 사업자등록증 인증 기능 추후 구현

        String businessCertificateImageUrl = null;
        if (file != null) {
            String prefix = "business-certificate/" + userId;
            businessCertificateImageUrl = fileStorageClient.upload(file, prefix);
        }

        Coordinates coordinates = geocodingClient.getCoordinateFrom(request.businessAddress());

        Business business = request.toEntity(coordinates, user, businessCertificateImageUrl);
        businessRepository.save(business);

        return BusinessCreateResponse.of(business.getId());
    }

    @Transactional(readOnly = true)
    public List<BusinessListResponse> getBusinessList() {
        List<Business> businesses = businessRepository.findAll();

        return businesses.stream()
                .map(BusinessListResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BusinessListResponse> getBusinessListByMe(Long userId) {
        return businessRepository.findByUserId(userId).stream()
                .map(BusinessListResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessDetailResponse getBusinessDetail(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));

        return BusinessDetailResponse.from(business);
    }
}
