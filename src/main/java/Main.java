import com.sun.net.httpserver.HttpServer;
import handlers.Static;
import handlers.WebRouteHandler;

import java.net.InetSocketAddress;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception{

        // create a server on port 8000

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new WebRouteHandler());
        server.createContext("/static", new Static());
        // set routes
        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();
    }
}
