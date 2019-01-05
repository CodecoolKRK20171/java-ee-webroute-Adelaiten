package handlers;

import annotations.WebRoute;
import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;

public class Router {
    private HttpExchange httpExchange;

    public Router(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }
    @WebRoute(path = "/index")
    public void onIndex() throws IOException {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("html/index.twig");
        JtwigModel model = JtwigModel.newModel();
        String response = template.render(model);
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
