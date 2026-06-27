package kr.co.webee.presentation.community.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.community.dto.response.ActiveUserResponse;
import kr.co.webee.presentation.community.dto.response.TrendingCategoryResponse;

import java.util.List;

@Tag(name = "Community", description = "커뮤니티 API")
public interface CommunityApi {

    @Operation(
            summary = "활동 중인 유저 조회",
            description = "최근 1시간 내 게시글 또는 댓글을 작성한 유저 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    List<ActiveUserResponse> getActiveUsers();

    @Operation(
            summary = "지금 뜨는 주제 조회",
            description = "최근 24시간 내 게시글이 많이 올라온 카테고리 Top 3를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    List<TrendingCategoryResponse> getTrendingCategories();
}
