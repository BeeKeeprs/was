package kr.co.webee.presentation.profile.crop.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserCropAddressListResponse(
        List<String> address
) {
    public static UserCropAddressListResponse of(List<String> address) {
        return UserCropAddressListResponse.builder()
                .address(address)
                .build();
    }
}
