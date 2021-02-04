package com.ishapirov.telegrambot.services.command;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class CommandService {
    @Resource()
    Map<ButtonAction, Command> commandMap;

    public Command getCommand(ButtonAction buttonAction){
        return commandMap.get(buttonAction);
    }
}
