package org.akriuchk.minishop;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("!prod")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(@Value("${service.api.version:}") String version,
                      @Value("${service.api.title:}") String title,
                      @Value("${service.api.description:}") String description,
                      @Value("${service.api.selector:}") String pathSelector) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("API")
                .apiInfo(apiInfo(version, title, description))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex(getPathSelector(pathSelector))))
                .build();
    }

    @Bean
    public Docket metrics(@Value("${service.api.selector:}") String pathSelector) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Metrics")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex(getPathSelector(pathSelector)))
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .validatorUrl(null)
                .build();
    }

    private String getPathSelector(String pathSelector) {
        if (!StringUtils.isEmpty(pathSelector)) {
            return pathSelector;
        }
        return ".*/(api|error|callback).*";
    }

    private ApiInfo apiInfo(String version, String title, String description) {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
    }
}
