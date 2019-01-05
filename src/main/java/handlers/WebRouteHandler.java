package handlers;

import annotations.WebRoute;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;

public class WebRouteHandler implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println(httpExchange.getRequestURI().getPath());
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();
        Class router = Router.class;
        Method[] methods = router.getMethods();
        for(Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for(Annotation annotation : annotations) {
                if(annotation instanceof WebRoute) {
                    WebRoute webRoute = (WebRoute) annotation;
                    if(webRoute.path().equals(path)){
                        try{
                            Constructor constructor = router.getDeclaredConstructor(new Class[] {HttpExchange.class});
                            Router routerObj = (Router) constructor.newInstance(new Object[]{httpExchange});
                            method.invoke(routerObj, null);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }




}
