package kr.co.webee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Profile("test")
public class AwsS3TestConfig {

    private static final DockerImageName LOCAL_STACK_IMAGE =
            DockerImageName.parse("localstack/localstack:s3-latest");

    @Bean(initMethod = "start", destroyMethod = "stop")
    LocalStackContainer localStackContainer() {
        return new LocalStackContainer(LOCAL_STACK_IMAGE)
                .withServices(LocalStackContainer.Service.S3);
    }

    @Bean
    S3Client s3Client(LocalStackContainer localStackContainer) {
        return S3Client.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
                .region(Region.of(localStackContainer.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                localStackContainer.getAccessKey(),
                                localStackContainer.getSecretKey())))
                .forcePathStyle(true)
                .build();
    }
}
