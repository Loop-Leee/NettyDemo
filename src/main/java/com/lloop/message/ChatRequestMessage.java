package com.lloop.message;

import lombok.Data;

/**
 * @Author lloop
 * @Create 2025/2/6 20:40
 */
@Data
public class ChatRequestMessage extends Message {

    private String content;
    private String to;
    private String from;

    public ChatRequestMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return Message.ChatRequestMessage;
    }

}
