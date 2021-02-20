package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class WebServer {

    private int port;

    public WebServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {

        WebServer webServer = new WebServer(8080);

        try {
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {

        System.out.println("Server running on port " + port);

        // Create server socket and client socket mirror listening for requests
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();

        // To read the browser requests
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Create output stream for the client socket
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        // Keep server running
        while (true) {

        String line = in.readLine();
            System.out.println(line);

            if (line.contains("GET / HTTP/1.1")) {

                // 1 - write header
                String header = "HTTP/1.1 200 OK\r\n" +
                        "ContentType: text/html\r\n" +
                        "\r\n";
                out.write(header.getBytes());

                // 2 - write home.html
                byte[] pageContent = Files.readAllBytes(Path.of("resources/home.html"));
                out.write(pageContent);

                out.flush();
                clientSocket.close();
                }
            }
        }
    }

/*
HTTP/1.0 200 Document Follows\r\n
Content-Type: text/html; charset=UTF-8\r\n
Content-Length: <file_byte_size> \r\n
\r\n

HTTP/1.0 200 Document Follows\r\n
Content-Type: image/<image_file_extension> \r\n
Content-Length: <file_byte_size> \r\n
\r\n


 */