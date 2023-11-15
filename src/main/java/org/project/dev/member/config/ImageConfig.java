package org.project.dev.member.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 외부저장소(로컬) 접근 권한(허용할지 말지)
public class ImageConfig implements WebMvcConfigurer {

    @Value("${file.profileImgUploadDir}")
    private String profileImgUploadDir;

    @Value("${file.reviewImgUploadDir}")
    private String reviewImgUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profileImages/**")
                .addResourceLocations("file:" + profileImgUploadDir);

        registry.addResourceHandler("/reviewImages/**")
                .addResourceLocations("file:" + reviewImgUploadDir);
    }
}
