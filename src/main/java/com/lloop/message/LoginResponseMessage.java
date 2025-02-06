package com.lloop.message;

import lombok.Data;

/**
 * @Author lloop
 * @Create 2025/2/6 20:40
 */
@Data
public class LoginResponseMessage extends Message {

    private boolean success;
    private String reason;

    public LoginResponseMessage(boolean success, String reason)
    {
        this.success = success;
        this.reason = reason;
    }

    @Override
    public int getMessageType(){
        return Message.LoginResponseMessage;
    }


}
