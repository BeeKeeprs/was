package kr.co.webee.presentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import kr.co.webee.presentation.annotation.ApiDocsErrorType;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.config.Elements.JWT;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    private final BuildProperties buildProperties;

    @Bean
    OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("Bearer")
                .bearerFormat(JWT)
                .name(AUTHORIZATION)
                .description("JWT 토큰을 입력해주세요 (Bearer 제외)");

        return new OpenAPI()
                .info(info())
                .addSecurityItem(new SecurityRequirement().addList(JWT))
                .components(new Components().addSecuritySchemes(JWT, securityScheme));
    }

    @Bean
    ModelResolver modelResolver(ObjectMapper objectMapper) { // Load formats from ObjectMapper
        return new ModelResolver(objectMapper);
    }

    @Bean
    OperationCustomizer successResponseBodyWrapper() {
        return (operation, handlerMethod) -> {
            if (!operation.getResponses().containsKey("200")) {
                return operation;
            }
            Content content = operation.getResponses().get("200").getContent();
            if (content == null) {
                return operation;
            }
            content.forEach((mediaTypeKey, mediaType) -> {
                Schema<?> originalSchema = mediaType.getSchema();
                Schema<?> wrappedSchema = new Schema<>();
                wrappedSchema.addProperty("code", new StringSchema().example("200"));
                wrappedSchema.addProperty("message", new StringSchema().example("요청이 성공하였습니다."));
                wrappedSchema.addProperty("data", originalSchema);
                mediaType.setSchema(wrappedSchema);
            });
            return operation;
        };
    }

    @Bean
    OperationCustomizer apiErrorTypeCustomizer() {
        return (operation, handlerMethod) -> {
            ApiDocsErrorType apiErrorType = handlerMethod.getMethodAnnotation(ApiDocsErrorType.class);
            if (apiErrorType == null) {
                return operation;
            }

            Stream.of(apiErrorType.value())
                    .sorted(Comparator.comparingInt(t -> t.getHttpStatus().value()))
                    .forEach(type -> {
                        Content content = new Content().addMediaType(APPLICATION_JSON_VALUE,
                                new MediaType()
                                        .schema(new Schema<>()
                                                .addProperty("code", new StringSchema().example(type.getHttpStatus().value() + ""))
                                                .addProperty("message", new StringSchema().example(type.getMessage()))
                                                .addProperty("data", new StringSchema().example("null"))
                                        ));

                        operation.getResponses().put(
                                type.getHttpStatus().value() + " " + type.name(),
                                new ApiResponse().description(type.getMessage()).content(content));
                    });
            return operation;
        };
    }

    private Info info() {
        return new Info()
                .title("WeBee API")
                .description("API 명세 문서 <br> 빌드 일자: " + buildProperties.getTime().atZone(ZoneId.of("Asia/Seoul"))
                        + "<br> 실행 일자: " + ZonedDateTime.now())
                .version(buildProperties.getVersion());
    }
}
