package kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.io;

import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.model.NongsaroCropPollinationParam;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.model.NongsaroCropPollinationParamList;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class NongsaroFileReader {
    private static final String CSV_PATH = "src/main/resources/nongsaro-crop-pollination-request-parameter-list.csv";

    public NongsaroCropPollinationParamList getNongsaroCropPollinationParamList() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(CSV_PATH));
            List<NongsaroCropPollinationParam> requestInfos = new ArrayList<>();

            for (String line : lines) {
                String[] values = line.split(",");

                String crop = values[0];
                String variety = values[1];
                String menuId = values[2];
                String cntntsNo = values[3];

                NongsaroCropPollinationParam entry = NongsaroCropPollinationParam.of(crop, variety, menuId, cntntsNo);
                requestInfos.add(entry);
            }
            return NongsaroCropPollinationParamList.of(requestInfos);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to read file.", ex);
        }
    }
}

