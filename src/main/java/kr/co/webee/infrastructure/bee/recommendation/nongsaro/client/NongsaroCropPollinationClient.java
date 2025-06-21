package kr.co.webee.infrastructure.bee.recommendation.nongsaro.client;

import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.dto.NongsaroCropPollinationDetailResponse;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.dto.NongsaroCropPollinationParamDto;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.dto.NongsaroCropPollinationResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class NongsaroCropPollinationClient {
    public static final String NONGSARO_CROP_POLLINATION_URL = "https://www.nongsaro.go.kr/portal/ps/psz/psza/contentMainDtlAjax.ps";
    private final RestClient restClient;

    public NongsaroCropPollinationClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(NONGSARO_CROP_POLLINATION_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    public NongsaroCropPollinationDetailResponse getCropPollinationDetail(NongsaroCropPollinationParamDto requestParam) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("menuId", requestParam.menuId());
        formData.add("cntntsNo", requestParam.cntntsNo());
        formData.add("viewType", "ajaxDtl");

        NongsaroCropPollinationResponse response = restClient.post()
                .body(formData)
                .retrieve()
                .body(NongsaroCropPollinationResponse.class);

        String cropPollinationDetailHtml = response.resultMap().resultList()[0].CLOBCTNX();
        List<BeeType> beeTypes = parseBeeTypeFrom(cropPollinationDetailHtml);
        String content = parseContentFrom(cropPollinationDetailHtml);

        return NongsaroCropPollinationDetailResponse.of(beeTypes, content);
    }

    private List<BeeType> parseBeeTypeFrom(String html) {
        Document doc = Jsoup.parse(html);
        Elements liElements = doc.select("li");

        List<BeeType> beeList = new ArrayList<>();

        for (Element li : liElements) {
            String text = li.text();

            if (text.startsWith("이용가능곤충:")) {
                String[] bees = text.replace("이용가능곤충:", "").trim().split("\\s*,\\s*");

                Arrays.stream(bees)
                        .forEach(bee -> beeList.add(BeeType.convertFrom(bee)));
            }
        }
        return beeList;
    }

    private String parseContentFrom(String html) {
        return Jsoup.parse(html).text();
    }
}