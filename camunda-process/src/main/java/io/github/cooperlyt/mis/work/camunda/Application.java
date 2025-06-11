package io.github.cooperlyt.mis.work.camunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@SpringBootApplication
@EnableProcessApplication
public class Application {

  public static void main(String... args){
    SpringApplication.run(Application.class,args);
  }

//  @Bean
//  public WebMvcConfigurer corsConfigurer() {
//    return new WebMvcConfigurerAdapter() {
//      @Override
//      public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedMethods("POST", "GET", "PUT", "DELETE", "HEAD")
//            .allowedOrigins("*")
//            .allowCredentials(false);
//      }
//    };
//  }



}
