package kr.co.webee.application.ai;

public record RagSearchOptions(
        String filterExpression,
        Integer topK,
        Double similarityThreshold
) {
    public RagSearchOptions {
        if (topK == null || topK <= 0) {
            throw new IllegalArgumentException("topK must be greater than 0! : " + topK);
        }
        if (similarityThreshold == null) similarityThreshold = 0.8;
    }
}
