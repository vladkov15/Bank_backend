package API;

import API.endpoint.*;
import com.sun.net.httpserver.*;
// import org.apache.commons.text.StringEscapeUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(38889), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        HttpContext context = server.createContext("/endpoint1", new Endpoint1());
        context = server.createContext("/endpoint2", new Endpoint2());
        context = server.createContext("/endpoint3",new Endpoint3());
        context = server.createContext("/endpoint4", new Endpoint4());
        context = server.createContext("/endpoint5", new Endpoint5());
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Server started");

    }



}