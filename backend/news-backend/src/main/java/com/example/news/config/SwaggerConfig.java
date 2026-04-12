package com.example.news.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("新闻中心网站 API")
                        .version("1.0.0")
                        .description("为 Vue 3 前端新闻网站提供后端 API 支持")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com")))
                .servers(List.of(
                        new Server().url("/api").description("默认服务器")
                ));
    }
}
