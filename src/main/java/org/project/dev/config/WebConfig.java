package org.project.dev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

   /*
    Todo
     1. code.aiaru@gmail.com
     2. 이미지 파일을 웹에서 접근할 수 있도록 설정하기 위한 클래스입니다.
     3. x
     4. x
     */

    @Value("${file.productImgUploadDir}")
    private String productImgUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/**")
                // 업로드되는 외부의 이미지들을 허가
                .addResourceLocations("file:" + productImgUploadDir)
                // static/image에 들어있는 이미지들을 허가.
                .addResourceLocations("classpath:/static/images/").setCachePeriod(60 * 60 * 24 * 365);
    }
}
