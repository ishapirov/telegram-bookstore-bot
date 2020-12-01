package com.ishapirov.telegrambot.config;

import com.ishapirov.telegrambot.domain.userviews.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
