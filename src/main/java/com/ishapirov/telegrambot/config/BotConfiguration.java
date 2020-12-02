package com.ishapirov.telegrambot.config;

import com.ishapirov.telegrambot.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class BotConfiguration {
    @Autowired
    private List<View> viewList;

    @Bean
    public Map<String, View> viewMap() {
        Map<String, View> viewMap = new HashMap<>();
        for (View view : viewList) {
            viewMap.put(view.getTypeString(), view);
        }
        return viewMap;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
