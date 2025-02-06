package com.lloop.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lloop
 * @Create 2025/2/6 18:46
 */
@Data
public abstract class Message implements Serializable {

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;

    static {
        messageClasses.put(LoginRequestMessage, LoginRequestMessage.class);
        messageClasses.put(LoginResponseMessage, LoginResponseMessage.class);
        messageClasses.put(ChatRequestMessage, ChatRequestMessage.class);
        messageClasses.put(ChatResponseMessage, ChatResponseMessage.class);
    }

}
