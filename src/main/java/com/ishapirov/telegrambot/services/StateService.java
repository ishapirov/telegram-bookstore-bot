package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.domain.userstate.State;
import com.ishapirov.telegrambot.domain.userstate.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StateService {

    @Autowired
    Map<State,UserState> stateSet;

    public UserState getState(State state){
        return stateSet.get(state);
    }

}
