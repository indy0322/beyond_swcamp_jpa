package com.ohgiraffers.springdatajpa.config;

import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
public class AppConfig {

    /* 설명. 미리 buld.gradle에 dependency로 추가해 둘 것 */
    /* 설명. DTO <-> Entity 매핑을 위한 modelmapper 라이브러리 bean 추가 */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
