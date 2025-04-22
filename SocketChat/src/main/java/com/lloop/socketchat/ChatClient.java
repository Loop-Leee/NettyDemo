package com.lloop.socketchat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8888);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // 启动一个线程接收服务器消息
            Receive receive = new Receive(socket);
            new Thread(receive).start();

            // 启动一个线程发送消息
            Send send = new Send(socket);
            Thread sendThread = new Thread(send);
            sendThread.start();
            sendThread.join();


        } catch (IOException | InterruptedException e) {
            System.out.println("连接服务器失败: " + e.getMessage());
        }
    }
}

class Send implements Runnable {
    private Socket socket;

    public Send(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in);) {

            while (true) {
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }
                out.println(input);
            }
        } catch (IOException e) {
            System.out.println("连接服务器失败: " + e.getMessage());
        }
    }
}

class Receive implements Runnable {
    private Socket socket;

    public Receive(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            System.out.println("服务器连接断开");
        }
    }
}