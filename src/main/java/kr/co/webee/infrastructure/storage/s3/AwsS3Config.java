package kr.co.webee.infrastructure.storage.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Profile("!test")
@Configuration
public class AwsS3Config {
    private final AwsBasicCredentials credentials;
    private final Region region;

    public AwsS3Config(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.credentials.region}") String region
    ) {
        this.credentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.region = Region.of(region);
    }

    @Bean
    S3Client s3Client() {
        return S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
