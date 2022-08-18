package com.github.jasgo.mindwave;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MindWave extends Thread {

    public static Socket socket;

    public MindWave(Socket socket) {
        MindWave.socket = socket;
    }

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(3000);
            while (true) {
                Socket socket = server.accept();
                Thread thread = new MindWave(socket);
                thread.start();
                new ListeningThread().start();
                new WritingThread().start();
                System.out.println("사용자가 접속했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ListeningThread extends Thread {
        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String readValue;
                while ((readValue = reader.readLine()) != null) {
                    System.out.println(readValue);
                }
            } catch (IOException e) {
                System.out.println("사용자와 연결이 종료되었습니다.");
            }
        }
    }

    public static class WritingThread extends Thread {
        @Override
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String readValue;
                while ((readValue = reader.readLine()) != null) {
                    writer.println(readValue);
                }
            } catch (IOException ignored) {
            }
        }
    }
}
