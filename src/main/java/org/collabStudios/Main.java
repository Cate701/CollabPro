package org.collabStudios;

import com.sun.net.httpserver.HttpServer;
import org.collabStudios.database.WorkspaceDBClient;
import org.collabStudios.database.WorkspaceService;
import org.collabStudios.model.Workspace;

import org.collabStudios.server.ApiHandler;
import org.collabStudios.server.SiteHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        WorkspaceDBClient client = new WorkspaceDBClient("testWs");
        Workspace ws = WorkspaceService.loadWorkspace(client);

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new SiteHandler(ws, client));
        server.createContext("/api/", new ApiHandler(ws, client));

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port 8000");
    }
}