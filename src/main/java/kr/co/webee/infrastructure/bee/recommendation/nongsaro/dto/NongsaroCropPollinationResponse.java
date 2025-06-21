package kr.co.webee.infrastructure.bee.recommendation.nongsaro.dto;

public record NongsaroCropPollinationResponse(
        ResultMap resultMap
) {
    public record ResultMap(
            ResultList[] resultList
    ){
        public record ResultList(
                String CLOBCTNX
        ){}
    }
}
