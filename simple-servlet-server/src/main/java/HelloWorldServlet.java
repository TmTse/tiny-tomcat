import com.tinyseries.tinytomcat.Request;
import com.tinyseries.tinytomcat.Response;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 简单测试类。
 */
public class HelloWorldServlet implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter writer=res.getWriter();
        writer.println("<h1>Hello World!</h1>");
//        writer.print("<h1>OK</h1>");
//        print方法并不会自动刷新，不调用flush就不会显示。
//        writer.flush();
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
