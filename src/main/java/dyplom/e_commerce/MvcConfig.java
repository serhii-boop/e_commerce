package dyplom.e_commerce;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path branUploadDir = Paths.get("./logo");
        String brandUploadPath = branUploadDir.toFile().getAbsolutePath();
        Path categoryUploadDir = Paths.get("./category");
        String categoryUploadPath = categoryUploadDir.toFile().getAbsolutePath();
        Path productUploadDir = Paths.get("./product");
        String productUploadPath = productUploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/product/**").addResourceLocations("file:/" + productUploadPath + "/");
        registry.addResourceHandler("/category/**").addResourceLocations("file:/" + categoryUploadPath + "/");
        registry.addResourceHandler("/logo/**").addResourceLocations("file:/" + brandUploadPath + "/");
    }
}
