package com.example.ec_sample.config;

import org.springframework.context.annotation.Configuration;  //Spring Frameworkのアノテーション＠Configurationを使うためのimport文
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//↑Spring MVCで静的ファイル(画像等)をどこから配信するかを設定するときに使うクラスを読み込むimport文
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//↑Spring MVCの設定をカスタマイズするためのインターフェースを読み込むimport文

@Configuration
 public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // /images/**へのアクセスをローカルフォルダにマッピング
        registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
    }
}
