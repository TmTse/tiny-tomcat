package com.tinyseries.tinytomcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 表示请求值
 */
public class Request {

    private InputStream input;
    private String uri;

//    private String request;

    public Request(InputStream input) {
        this.input = input;
    }

    public void parse() {
        StringBuilder request = new StringBuilder(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j=0; j<i; j++) {
            request.append((char) buffer[j]);
        }

        //错误的方法，会被read方法阻塞，等待未关闭的socket
        /*StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            while((i = input.read(buffer))!=-1){
                for (int j=0; j<i; j++) {
                    request.append((char) buffer[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        System.out.println(request.toString());
//        this.request=request.toString();
        uri = parseUri(request.toString());


       /* StringBuilder request = new StringBuilder(2048);
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            while ((line=reader.readLine())!=null){
                request.append(line);
                request.append("\r\n");
                System.out.println(line);
            }
            uri = parseUri(request.toString());
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

}