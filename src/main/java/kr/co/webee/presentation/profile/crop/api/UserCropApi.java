package kr.co.webee.presentation.profile.crop.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.annotation.UserId;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropCreateRequest;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropUpdateRequest;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropAddressListResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropCreateResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropDetailResponse;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropListResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Tag(name = "UserCrop", description = "사용자 재배 작물 정보 관련 API")
public interface UserCropApi {
    @Operation(
            summary = "사용자 재배 작물 정보 등록",
            description = "사용자가 재배하는 작물 정보를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 재배 작물 등록 성공"),
    })
    UserCropCreateResponse createUserCrop(
            @Parameter(description = "등록할 사용자 재배 작물 정보", required = true)
            @RequestBody @Valid UserCropCreateRequest request,

            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "사용자 재배 작물 정보 목록 조회",
            description = "사용자가 재배하는 작물 정보 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 재배 작물 목록 조회 성공"),
    })
    List<UserCropListResponse> getUserCropList(
            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "사용자 재배 작물 정보 상세 조회",
            description = "사용자가 재배하는 작물 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 재배 작물 정보 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사용자 재배 작물 없음")
    })
    UserCropDetailResponse getUserCropDetail(
            @Parameter(description = "사용자 재배 작물 ID", example ="1", required = true)
            @PathVariable Long userCropId
    );

    @Operation(
            summary = "사용자 작물 재배지 목록 조회",
            description = "사용자가 재배하는 작물의 재배지 목록 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 작물 재배지 목록 조회 성공"),
    })
    UserCropAddressListResponse getUserCropAddressList(
            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "사용자 재배 작물 정보 수정",
            description = "사용자가 재배하는 작물 상세 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 재배 작물 정보 수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사용자 재배 작물 없음"),
            @ApiResponse(responseCode = "403", description = "수정 권한 없음")
    })
    void updateUserCrop(
            @Parameter(description = "사용자 재배 작물 ID", example ="1", required = true)
            @PathVariable Long userCropId,

            @Parameter(description = "수정할 사용자 재배 작물 정보", required = true)
            @RequestBody @Valid UserCropUpdateRequest request,

            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "사용자 재배 작물 정보 삭제",
            description = "사용자가 재배하는 작물 상세 정보를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 재배 작물 정보 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사용자 재배 작물 없음"),
            @ApiResponse(responseCode = "403", description = "삭제 권한 없음")
    })
    void deleteUserCrop(
            @Parameter(description = "사용자 재배 작물 ID", example ="1", required = true)
            @PathVariable Long userCropId,

            @Parameter(hidden = true)
            @UserId Long userId
    );
}
