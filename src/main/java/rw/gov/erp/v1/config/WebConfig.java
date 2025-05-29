package rw.gov.erp.v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/**") // Allow all endpoints
        .allowedOrigins("*") // For production, whitelist domains
        .allowedMethods("*")
        .allowedHeaders("*");
  }

  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
        .addResourceLocations(
            "file:uploads/",
            "classpath:/static",
            "classpath:/META-INF/resources/");
  }

  @Override
  public void addViewControllers(@NonNull ViewControllerRegistry registry) {
    registry.addViewController("/docs").setViewName("redirect:/swagger-ui.html");
  }
}
