package io.github.cooperlyt.mis.service.dictionary;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI restfulOpenAPI(){
        return new OpenAPI()
            .info(new Info().title("字典服务接口")
                .description("字典和行政区划服务")
                .version("v0.0.1"))
            .externalDocs(new ExternalDocumentation()
                .description("SpringDoc Wiki Documentation")
                .url("https://springdoc.org/v2"));
    }
}
