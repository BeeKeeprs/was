package kr.co.webee.presentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.config.Elements.JWT;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Configuration

public class SwaggerConfig {
    private final BuildProperties buildProperties;

    @Value("${was.api}")
    private List<String> apiUrls;

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
                .servers(
                        apiUrls.stream()
                                .map(url -> new io.swagger.v3.oas.models.servers.Server().url(url))
                                .toList()
                )
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

    @Bean(name = "pageableOpenApiCustomizer")
    OpenApiCustomizer pageableCustomizer() {
        return openApi -> customizeOperations(openApi, "pageable", (operation, parameter) -> {
            operation.getParameters().remove(parameter);
            pageableParameters().forEach(operation::addParametersItem);
        });
    }

    private List<Parameter> pageableParameters() {
        return List.of(
                new QueryParameter()
                        .name("page")
                        .schema(new StringSchema().example("0").description("요청한 페이지의 번호")),
                new QueryParameter()
                        .name("size")
                        .schema(new StringSchema().example("20").description("페이지당 항목 수")),
                new QueryParameter()
                        .name("sort")
                        .schema(new StringSchema()
                                .example("asc")
                                .description("정렬 기준 형식: 속성명,(asc|desc). 여러 정렬 기준 지원. 기본값은 오름차순"))
        );
    }

    private void customizeOperations(OpenAPI openApi, String paramName, BiConsumer<Operation, Parameter> customizer) {
        openApi.getPaths().values().stream()
                .flatMap(pathItem -> pathItem.readOperations().stream())
                .filter(op -> op.getParameters() != null)
                .forEach(op -> op.getParameters().stream()
                        .filter(p -> paramName.equals(p.getName()))
                        .findFirst()
                        .ifPresent(p -> customizer.accept(op, p)));
    }

    private Info info() {
        return new Info()
                .title("WeBee API")
                .description("API 명세 문서 <br> 빌드 일자: " + buildProperties.getTime().atZone(ZoneId.of("Asia/Seoul"))
                        + "<br> 실행 일자: " + ZonedDateTime.now())
                .version(buildProperties.getVersion());
    }
}
