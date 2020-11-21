package com.ishapirov.telegrambot.bot;

import com.ishapirov.telegrambot.services.UpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;



@Component
public class Bot extends TelegramWebhookBot {
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.path}")
    private String path;

    @Autowired
    private UpdateHandler updateHandler;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateHandler.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotPath() {
        return path;
    }
}