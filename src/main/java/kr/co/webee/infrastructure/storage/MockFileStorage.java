package kr.co.webee.infrastructure.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class MockFileStorage implements FileStorage {

    @Override
    public String upload(MultipartFile file) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String fakeStoredName = UUID.randomUUID() + "_" + originalFilename;

        // 실제 파일 저장은 하지 않음
        log.info("[MockFileStorage] 파일 '{}' 을 '{}' 으로 저장한 것으로 간주합니다.", originalFilename, fakeStoredName);

        return "https://cataas.com/cat?width=600";
    }
}
