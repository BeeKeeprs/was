package kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.io;

import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.model.NongsaroCropPollinationParam;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.model.NongsaroCropPollinationParamList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class NongsaroFileReader {
    @Value("${bee.recommendation.nongsaro.request-parameter-list}")
    private Resource nongsaroFileResource;

    public NongsaroCropPollinationParamList getNongsaroCropPollinationParamList() {
        try (InputStream inputStream = nongsaroFileResource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            List<NongsaroCropPollinationParam> requestInfos = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                String crop = values[0];
                String variety = values[1];
                String menuId = values[2];
                String cntntsNo = values[3];

                NongsaroCropPollinationParam entry = NongsaroCropPollinationParam.of(crop, variety, menuId, cntntsNo);
                requestInfos.add(entry);
            }

            return NongsaroCropPollinationParamList.of(requestInfos);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to read file.", ex);
        }
    }
}