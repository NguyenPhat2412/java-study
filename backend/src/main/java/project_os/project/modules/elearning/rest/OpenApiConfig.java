package project_os.project.modules.elearning.rest;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình OpenAPI (Swagger 3) tự động tạo tài liệu và giao diện Swagger UI cho toàn bộ REST API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI elearningOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Learning Management REST API")
                        .description("Tài liệu và giao diện kiểm thử tự động danh sách các API Endpoints trong hệ thống E-Learning.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nguyễn Văn Phát")
                                .email("contact@elearning.vn"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}
