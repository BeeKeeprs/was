package kr.co.webee.presentation.news.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.news.dto.response.NewsArticleDetailResponse;
import kr.co.webee.presentation.news.dto.response.NewsArticleListResponse;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "News", description = "뉴스 기사 API")
public interface NewsApi {

    @Operation(
            summary = "뉴스 기사 목록 조회",
            description = "키워드에 해당하는 뉴스 기사 목록을 Slice 단위로 조회합니다. 기본값은 size=5, 발행일 내림차순입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "뉴스 기사 목록 조회 성공"),
    })
    Slice<NewsArticleListResponse> getAllNewsArticleList(
            @Parameter(description = "검색 키워드", example = "꿀벌", required = true)
            @RequestParam String keyword,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "5")
            @RequestParam(defaultValue = "5") int size
    );

    @Operation(
            summary = "뉴스 기사 상세 조회",
            description = "뉴스 기사 ID에 해당하는 기사 상세를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "뉴스 기사 조회 성공"),
            @ApiResponse(responseCode = "404", description = "뉴스 기사 없음"),
    })
    NewsArticleDetailResponse getNewsArticleDetail(
            @Parameter(description = "뉴스 기사 ID", example = "101", required = true)
            @PathVariable Long newsArticleId
    );
}
