package com.example.userCenter.server.impl;

import com.example.userCenter.model.User;
import com.example.userCenter.server.UserServer;
import org.springframework.stereotype.Service;

@Service
public class UserServerImpl implements UserServer {
    @Override
    public boolean login(User user) {
        return "root".equals(user.getName()) && "root".equals(user.getPwd());
    }
}
