package com.tanmayvijayvargiya.factseveryday.event;

import com.tanmayvijayvargiya.factseveryday.vo.User;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class UserSyncedEvent {
    User user;

    public UserSyncedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
