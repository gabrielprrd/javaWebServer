package org.academiadecodigo.bootcamp.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebServer {

    private int port;
    ServerSocket serverSocket;
    BufferedReader in;
    DataOutputStream out;

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
        serverSocket = new ServerSocket(port);

        // Keep server running
        while (true) {
            handleRequests();

        }
    }

    public void handleRequests() throws IOException {

            Socket clientSocket = serverSocket.accept();
            // To read the browser requests
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Create output stream for the client socket
            out = new DataOutputStream(clientSocket.getOutputStream());

            String line = in.readLine();

        if (line.contains("GET")) {

            String route = line.split(" ")[1];
            System.out.println(line);

            if (route.equals("/")) {
                getHomePage();

            } if (route.equals("/favicon.ico")) {
                getFavicon();

            } if (route.contains("/images")) {
                handleImages(route);

            } else {

                // Check if there is a file with the same name as the route
                    if (pageExists(route)) {
                        getPage(route);

                    } else {
                        get404Page();
                    }

                }
            }
    }

    public void getFavicon() throws IOException {
        byte[] favicon = Files.readAllBytes(Path.of("resources/favicon/favicon.ico"));

        String header = "HTTP/1.0 200 Document Follows\r\n" +
                "Content-Type: image/x-icon \r\n" +
                "Content-Length: " + favicon.length + " \r\n" +
                "\r\n";

        out.write(header.getBytes());
        out.write(favicon);

        out.flush();
    }

    public void handleImages(String route) throws IOException {

        // browser makes a new request to the image folder based on root
        byte[] image = Files.readAllBytes(Path.of(route.substring(1)));

        String header = "HTTP/1.0 200 Document Follows\r\n" +
                "Content-Type: image/<image_file_extension> \r\n" +
                "Content-Length: " + image.length + " \r\n" +
                "\r\n";

        out.write(header.getBytes());
        out.write(image);

        out.flush();
    }

    public void getHomePage() throws IOException {

        byte[] pageContent = Files.readAllBytes(Path.of("resources/pages/home.html"));

        String header = "HTTP/1.1 200 OK\r\n" +
                "ContentType: text/html\r\n" +
                "Content-Length: " + pageContent.length + " \r\n" +
                "\r\n";

        out.write(header.getBytes());
        out.write(pageContent);

        out.flush();
    }

    public void getPage(String route) throws IOException {

        String path = Path.of("resources/pages" + route).toString() + ".html";

        byte[] pageContent = Files.readAllBytes(Path.of(path));

        String header = "HTTP/1.1 200 OK\r\n" +
                "ContentType: text/html\r\n" +
                "Content-Length: " + pageContent.length + " \r\n" +
                "\r\n";

        out.write(header.getBytes());
        out.write(pageContent);

        out.flush();
    }

    public void get404Page() throws IOException {

        byte[] pageContent = Files.readAllBytes(Path.of("resources/pages/404.html"));
        String header = "HTTP/1.0 404 Not Found\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + pageContent.length + " \r\n" +
                "\r\n";

        out.write(header.getBytes());
        out.write(pageContent);

        out.flush();
    }

    public boolean pageExists(String route) {

        File[] dirArray = new File("resources/pages").listFiles();

        assert dirArray != null;
        for (File file : dirArray) {
            if (("/" + file.getName()).equals(route + ".html")) return true;
        }
        return false;
    }
}
