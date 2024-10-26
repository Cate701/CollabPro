package org.collabStudios.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.collabStudios.database.WorkspaceDBClient;
import org.collabStudios.model.Workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Paths;

public class SiteHandler implements HttpHandler {

    Workspace workspace;
    WorkspaceDBClient workspaceDBClient;

    public SiteHandler(Workspace ws, WorkspaceDBClient client) {
        workspace = ws;
        workspaceDBClient = client;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        if (path.equals("/")) {
            path = "/index.html";
        }

        String root = Paths.get("src/main/webapp").toAbsolutePath().toString();
        File file = new File(root + path).getCanonicalFile();

        if (!file.getPath().startsWith(root)) {
            // Suspected path traversal attack: reject with 403 error.
            String response = "403 (Forbidden)\n";
            exchange.sendResponseHeaders(403, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else if (!file.isFile()) {
            // Object does not exist or is not a file: reject with 404 error.
            String response = "404 (Not Found)\n";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // Object exists and is a file: accept with response code 200.
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            FileInputStream fs = new FileInputStream(file);
            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer,0,count);
            }
            fs.close();
            os.close();
        }
    }
}
