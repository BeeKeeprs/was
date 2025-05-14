package kr.co.webee.infrastructure.storage.s3;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.infrastructure.storage.FileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Component
public class S3FileStorageClient implements FileStorageClient {
    private final S3Client s3Client;
    private final String bucketName;
    private final String cdnUrl;

    public S3FileStorageClient(S3Client s3Client,
                               @Value("${cloud.aws.s3.bucket}") String bucketName,
                               @Value("${cloud.aws.s3.cdn-url}") String cdnUrl) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.cdnUrl = cdnUrl;
    }

    @Override
    public String upload(MultipartFile file, String prefix) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = prefix + "/" + fileName;

            s3Client.putObject(request -> request.bucket(bucketName).key(filePath),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return cdnUrl + "/" + filePath;
        } catch (Exception e) {
            log.error("Failed to upload file to S3", e);
            throw new BusinessException(ErrorType.FILE_UPLOAD_FAILED, "Failed to upload file to S3");
        }
    }
}
