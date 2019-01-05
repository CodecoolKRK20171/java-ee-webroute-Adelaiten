package handlers;

import Annotations.WebRoute;
import com.sun.net.httpserver.HttpExchange;

public class Router {
    private HttpExchange httpExchange;

    public Router(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }
    @WebRoute(path = "/index")
    public void onIndex() {

    }

}
