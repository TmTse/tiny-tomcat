package com.tinyseries.tinytomcat;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * Servlet的处理类
 */
public class ServletProcessor {

    /**
     * Servlet处理方法。
     *
     * @param request
     * @param response
     */
    public void process(Request request, Response response) {
        //解析Servlet类名
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;

        try {
            // create a URLClassLoader
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            //类加载器加载路径
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString() ;
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
        Class clazz = null;
        try {
            //加载Servlet类
            clazz = loader.loadClass(servletName);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        //将Request和Response转化为RequestFacade和ResponseFacade
        RequestFacade requestFacade = new RequestFacade(request);
        ResponseFacade responseFacade = new ResponseFacade(response);
        try {
            //初始化Servlet类
            Servlet servlet = (Servlet) clazz.newInstance();
            //写入响应头部，否则浏览器无法解析。
            PrintWriter writer=response.getWriter();
            writer.print("HTTP/1.1 200 OK\r\n");
            writer.print("Content-Type: text/html\r\n");
            writer.print("\r\n");
            //print方法不会自动刷新。
            writer.flush();
            //调用Servlet类中service方法。
            servlet.service( requestFacade, responseFacade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}