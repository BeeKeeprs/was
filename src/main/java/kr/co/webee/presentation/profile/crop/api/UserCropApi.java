package kr.co.webee.presentation.profile.crop.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.annotation.UserId;
import kr.co.webee.presentation.profile.crop.dto.request.UserCropRequest;
import kr.co.webee.presentation.profile.crop.dto.response.UserCropListResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "UserCrop")
public interface UserCropApi {
    @Operation(summary = "사용자 재배 작물 정보 등록", description = "사용자가 재배하는 작물 정보를 등록하는 API")
    @ApiDocsErrorType(ErrorType.ENTITY_NOT_FOUND)
    void addUserCrop(@RequestBody @Valid UserCropRequest request, @UserId Long userId);

    @Operation(summary = "사용자 재배 작물 정보 목록 조회", description = "사용자가 재배하는 작물 정보 목록을 조회하는 API")
    @ApiDocsErrorType(ErrorType.ENTITY_NOT_FOUND)
    List<UserCropListResponse> getUserCropList(@UserId Long userId);
}
