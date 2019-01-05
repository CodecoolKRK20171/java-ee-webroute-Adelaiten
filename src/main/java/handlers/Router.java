package handlers;

import annotations.WebRoute;
import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

    @WebRoute(path = "/index", method = "POST")
    public void onLogin() throws IOException {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("html/login.twig");
        JtwigModel model = JtwigModel.newModel();
        Map inputs = getData(httpExchange);
        model.with("name", inputs.get("name"));
        String response = template.render(model);
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private Map<String, String> getData(HttpExchange httpExchange) throws IOException{
        InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String formData = bufferedReader.readLine();
        return parseFormData(formData);
    }
}
