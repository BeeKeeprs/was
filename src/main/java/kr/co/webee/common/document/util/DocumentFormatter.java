package kr.co.webee.common.document.util;


import kr.co.webee.presentation.document.dto.VectorDocumentResponse;

import java.util.List;

public class DocumentFormatter {

    public static <T> String formatString(String header, List<String> documents) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(header).append('\n');
        documents.forEach(document -> stringBuilder.append(document).append('\n'));

        return stringBuilder.toString();
    }

    public static String formatVectorDocs(String header, List<VectorDocumentResponse> documents) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(header).append('\n');
        documents.forEach(document -> stringBuilder.append(document.content()).append('\n'));

        return stringBuilder.toString();
    }
}