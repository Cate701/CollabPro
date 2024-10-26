package org.collabStudios;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.collabStudios.database.WorkspaceDBClient;
import org.collabStudios.database.WorkspaceService;
import org.collabStudios.model.Workspace;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.collabStudios.server.ApiHandler;
import org.collabStudios.server.SiteHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.URIParameter;
import java.util.List;

public class Main {

    private static void sqlTestCreate() {
        WorkspaceDBClient client = new WorkspaceDBClient("testWs");
        System.out.println("Connection");
        Workspace dummyNew = new Workspace("testWs", List.of(new String[]{"frontend", "backend", "database"}), null, null, 5);
        WorkspaceService.setupNewWorkspaceBlank(client,dummyNew);
        client.close();
    }

    private static void sqlTestRead() {
        WorkspaceDBClient client = new WorkspaceDBClient("testWs");
        Workspace ws = WorkspaceService.loadWorkspace(client);

        System.out.println("WOOO");
        System.out.println(ws.getName());
        System.out.println(ws.getSkills().size());
        System.out.println(ws.getUsers().get(0).getName());
        System.out.println(ws.getUsers().get(0).getSkill("frontend"));
        System.out.println(ws.getUsers().get(0).getSkill("backend"));
        System.out.println(ws.getUsers().get(0).getSkill("database"));
        System.out.println(ws.getTasks().get(0).getAssignedUsers().get(0).getName());
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new SiteHandler());
        server.createContext("/api/", new ApiHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port 8000");
    }
}