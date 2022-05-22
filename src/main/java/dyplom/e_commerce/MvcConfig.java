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

        registry.addResourceHandler("/logo/**").addResourceLocations("file:/" + brandUploadPath + "/");
    }
}
