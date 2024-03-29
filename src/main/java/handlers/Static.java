package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import helpers.MimeTypeResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

public class Static implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        System.out.println("Looking for " + uri.getPath());
        String path = "." + uri.getPath();

        ClassLoader classLoader = getClass().getClassLoader();
        URL fileURL = classLoader.getResource(path);
        OutputStream outputStream = httpExchange.getResponseBody();
        if(fileURL == null) {
            send404(httpExchange);
        }else {
            sendFile(httpExchange, fileURL);
        }
    }

    private void send404(HttpExchange httpExchange) throws IOException{
        String response = "(404) Not found\n";
        httpExchange.sendResponseHeaders(404, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.toString().getBytes());
        outputStream.close();
    }

    private void sendFile(HttpExchange httpExchange, URL fileUrl) throws IOException{
        File file = new File(fileUrl.getFile());
        MimeTypeResolver mimeTypeResolver = new MimeTypeResolver(file);
        String mime = mimeTypeResolver.getMimeType();

        httpExchange.getResponseHeaders().set("Content-Type", mime);
        httpExchange.sendResponseHeaders(200, 0);

        OutputStream os = httpExchange.getResponseBody();

        // send the file
        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = fs.read(buffer)) >= 0) {
            os.write(buffer,0,count);
        }
        os.close();
    }

}
