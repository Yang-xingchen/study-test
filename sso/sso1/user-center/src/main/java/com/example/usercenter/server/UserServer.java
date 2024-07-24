package com.example.usercenter.server;

import com.example.usercenter.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServer {

    public boolean login(User user) {
        return "root".equals(user.getName()) && "root".equals(user.getPwd());
    }

}
