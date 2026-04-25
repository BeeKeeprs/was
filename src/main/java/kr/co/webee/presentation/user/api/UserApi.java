package kr.co.webee.presentation.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.user.dto.response.UserProfileImageUploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "사용자 API")
public interface UserApi {

    @Operation(
            summary = "프로필 사진 업로드",
            description = "로그인 사용자의 프로필 사진을 S3에 업로드하고 URL을 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "이미지 파일 누락 또는 잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "502", description = "S3 업로드 실패")
    })
    @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    UserProfileImageUploadResponse uploadProfileImage(
            @Parameter(
                    description = "업로드할 프로필 이미지 파일",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("image") MultipartFile image,
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(
            summary = "회원 탈퇴 (soft delete)",
            description = "로그인 사용자를 soft delete 처리합니다. 실제 레코드는 삭제하지 않고 deletedAt만 기록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @DeleteMapping("/me")
    String withdraw(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(hidden = true) HttpServletResponse response
    );
}
