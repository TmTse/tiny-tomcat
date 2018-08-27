package com.tinyseries.tinytomcat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    //定义一个资源存放路径，用来存放静态资源，
    static final File WEB_ROOT = new File("d:\\webRoot");
    private static final String SHUTDOWN_COMMAND = "shutdown";
    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer httpServer=new HttpServer();
        httpServer.await();
    }

    public void await() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            serverProcess(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serverProcess(ServerSocket serverSocket) {
        while (!shutdown) {
            try (Socket socket = serverSocket.accept()) {
                System.out.println(socket.hashCode());
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                Request request = new Request(input);
                request.parse();
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
