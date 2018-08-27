package com.tinyseries.tinytomcat;

import java.io.*;


/**
 * 表示返回值
 */
public class Response {
    private static final int BUFFER_SIZE = 1024;
    private Request request;
    private OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        //读取访问地址请求的文件
        File file = new File(HttpServer.WEB_ROOT, request.getUri());
        try (FileInputStream fis = new FileInputStream(file)){
            if (file.exists()) {
                //如果文件存在
                //添加相应头。
                StringBuilder heads=new StringBuilder("HTTP/1.1 200 OK\r\n");
                heads.append("Content-Type: text/html\r\n");
                //头部
                StringBuilder body=new StringBuilder();
                //读取相应主体
                int len ;
                while ((len=fis.read(bytes, 0, BUFFER_SIZE)) != -1) {
                    body.append(new String(bytes,0,len));
                }
                //添加Content-Length
                heads.append(String.format("Content-Length: %d\n",body.toString().getBytes().length));
                heads.append("\r\n");
                output.write(heads.toString().getBytes());
                output.write(body.toString().getBytes());
            } else {
                response404(output);
            }
        }catch (FileNotFoundException e){
            response404(output);
        }
    }

    private void response404(OutputStream output) throws IOException {
        StringBuilder response=new StringBuilder();
        response.append("HTTP/1.1 404 File Not Found\r\n");
        response.append("Content-Type: text/html\r\n");
        response.append("Content-Length: 23\r\n");
        response.append("\r\n");
        response.append("<h1>File Not Found</h1>");
        output.write(response.toString().getBytes());
    }
}