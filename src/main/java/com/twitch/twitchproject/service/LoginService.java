package com.twitch.twitchproject.service;

import com.twitch.twitchproject.dao.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class LoginService {
    @Autowired
    private LoginDao loginDao;

    public String verifyLogin(String userId, String password) throws IOException {
        return loginDao.verifyLogin(userId, password);
    }
}

