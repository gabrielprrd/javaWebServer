package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebServer {

    private int port;
    private URL url;
    HttpURLConnection connection;

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

        //url = new URL("http://localhost:8080");
        //connection = (HttpURLConnection) url.openConnection();
        //connection.setRequestMethod("GET");

        System.out.println("Server running on port " + port);

        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();

        DataOutputStream out = new DataOutputStream(new OutputStream(){
            @Override
            public void write(int b) throws IOException {

            }
        });
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while (true) {

            //String message = in.readLine();
            //System.out.println(message);
        }
    }



}
