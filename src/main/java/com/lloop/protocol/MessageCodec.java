package com.lloop.protocol;

import com.lloop.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @Author lloop
 * @Create 2025/2/6 18:45
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodec extends ByteToMessageCodec<Message> {

    // 元数据 16 字节(魔数+版本+序列化方式+指令类型+消息id+数据长度)
    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 1. 5 字节的魔数 (lloop)
        out.writeBytes(new byte[]{108, 108, 111, 111, 112});
        // 2. 1 字节的版本
        out.writeByte(1);
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        out.writeByte(0);
        // 4. 1 字节的指令类型
        out.writeByte(msg.getMessageType());
        // 5. 4 个字节的消息顺序管理/重复消息检测
        out.writeInt(msg.getSequenceId());
        // 6. 获取内容的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();   // 用于将字节流转为字节数组(决定字节数据的去向)
        ObjectOutputStream oos = new ObjectOutputStream(bos);      // 用于将对象转为字节数据
        oos.writeObject(msg);                                      // Message -> bytes
        byte[] bytes = bos.toByteArray();                          // bytes -> byte[]
        // 7. 4 个字节的内容长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);
        out.add(message);
    }
}
