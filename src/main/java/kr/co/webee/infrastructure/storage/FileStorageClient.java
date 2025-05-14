package kr.co.webee.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageClient {
    /**
     * 파일을 저장하고 URL 또는 경로를 반환한다.
     */
    String upload(MultipartFile file, String prefix);
}
