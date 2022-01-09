package com.twitch.twitchproject.service;

import com.twitch.twitchproject.dao.RegisterDao;
import com.twitch.twitchproject.entity.db.User;
import com.twitch.twitchproject.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RegisterService {
    private RegisterDao registerDao;

    @Autowired
    public RegisterService(RegisterDao registerDao) {
        this.registerDao = registerDao;
    }

    public boolean register(User user) throws IOException {
        user.setPassword(Util.encryptPassword(user.getUserId(), user.getPassword()));
        return registerDao.register(user);
    }
}
