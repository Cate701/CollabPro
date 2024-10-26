package org.collabStudios.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

public class SiteHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        if (path.isBlank()) {
            path = "index.html";
        }



    }
}
