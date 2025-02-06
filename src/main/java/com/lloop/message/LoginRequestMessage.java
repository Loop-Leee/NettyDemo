package com.lloop.message;

import lombok.Data;

/**
 * @Author lloop
 * @Create 2025/2/6 20:38
 */
@Data
public class LoginRequestMessage extends Message {

    private String username;
    private String password;

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return Message.LoginRequestMessage;
    }

}
