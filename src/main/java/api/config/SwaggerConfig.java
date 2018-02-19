package api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("api.controllers"))
                //.paths(regex("/index.*"))
                .build()
                .apiInfo(metaData());
    }
    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Promethean Cloud Solutions - WhatsNext")
                .description("WhatsNext - path to win elections")
                .termsOfServiceUrl("Copyright Promethean Cloud. All Rights Reserved.")
                .version("1.0")
                .contact("info@prometheancloud.com")
                .build();
        return apiInfo;
    }
}
