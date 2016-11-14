package in.appdirect.movierental;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@SpringBootApplication
@EnableSwagger2
@ComponentScan("in.appdirect.*")
public class Application {
 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
     
    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("appdirect-movieRentalServices")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/appDirect.*"))
                .build();
    }
     
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Appdirect Movie Rental Services Subscription")
                .description("Appdirect Movie Rental Services Subscription")
                .contact("Malviya, Rahul")
                .build();
    }
}