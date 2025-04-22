package com.lloop.socketchat;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 8888;
    // 用线程安全集合保存所有客户端输出流
    private static final CopyOnWriteArraySet<PrintWriter> clientWriters = new CopyOnWriteArraySet<>();
    // 使用线程池管理客户端连接线程
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        System.out.println("聊天室服务器启动，监听端口 " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // 阻塞等待客户端连接
                Socket client = serverSocket.accept();
                // 调用线程处理客户端连接
                pool.execute(new ClientHandler(client));
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;      // 客户端Socket
        private String name;        // 客户端用户名
        private BufferedReader in;  // 客户端输入流
        private PrintWriter out;    // 客户端输出流

        /**
         * 客户端连接处理线程
         * @param socket 客户端Socket
         */
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        /**
         * 客户端连接处理逻辑
         */
        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("请输入用户名:");
                name = in.readLine();
                if (name == null || name.trim().isEmpty()) {
                    out.println("用户名无效，连接关闭。");
                    return;
                }

                clientWriters.add(out);
                broadcast("【系统】" + name + " 加入了聊天室");

                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(name + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("连接异常: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                clientWriters.remove(out);
                broadcast("【系统】" + name + " 离开了聊天室");
            }
        }

        /**
         * 广播消息给所有客户端
         * @param message 消息内容
         */
        private void broadcast(String message) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }
}
