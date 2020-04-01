package com.example.userCenter.server;

import com.example.userCenter.model.User;
import org.springframework.stereotype.Service;

public interface UserServer {

    boolean login(User user);

}
