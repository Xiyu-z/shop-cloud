package com.shop.file.config;

import com.shop.core.constant.Constants;
import com.shop.file.common.PicCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Autowired
    private PicCommon common;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        PicCommon.ElPath path = common.getPath();
        if (path == null || path.getPath() == null) {
            return;
        }
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + path.getPath());
    }

}
