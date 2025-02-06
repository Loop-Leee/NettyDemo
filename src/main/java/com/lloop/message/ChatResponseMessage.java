package com.lloop.message;

import lombok.Data;

/**
 * @Author lloop
 * @Create 2025/2/6 20:41
 */
@Data
public class ChatResponseMessage extends Message {

    private String content;
    private String from;
    private String to;

    public ChatResponseMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return Message.ChatResponseMessage;
    }

}
