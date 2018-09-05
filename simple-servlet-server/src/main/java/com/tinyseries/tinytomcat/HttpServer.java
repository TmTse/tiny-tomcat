package com.tinyseries.tinytomcat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单http服务器。
 */
public class HttpServer {
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
//                System.out.println(socket.hashCode());
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                //创建Request对象
                Request request = new Request(input);
                request.parse();
                //创建Response对象
                Response response = new Response(output);
                response.setRequest(request);
                if (request.getUri().startsWith("/servlet/")) {
                    //如果地址以/servlet开头就作为Servlet处理
                    ServletProcessor processor = new ServletProcessor();
                    processor.process(request, response);
                }else {
                    //否则作为静态资源使用
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
