package com.ishapirov.telegrambot.config;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.views.TelegramView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class BotConfiguration {
    @Autowired
    private List<TelegramView> telegramViewList;
    @Autowired
    private List<Command> commandList;

    @Bean
    public Map<String, TelegramView> telegramViewMap() {
        Map<String, TelegramView> telegramViewMap = new HashMap<>();
        for (TelegramView telegramView : telegramViewList)
            telegramViewMap.put(telegramView.getTypeString(), telegramView);
        return telegramViewMap;
    }

    @Bean
    public Map<ButtonAction, Command> commandMap() {
        Map<ButtonAction, Command> commandMap = new HashMap<>();
        for(Command command: commandList)
            commandMap.put(command.getButtonActionTrigger(),command);
        return commandMap;
    }

}
