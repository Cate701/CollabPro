package org.collabStudios.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.collabStudios.database.WorkspaceDBClient;
import org.collabStudios.model.Task;
import org.collabStudios.model.User;
import org.collabStudios.model.Workspace;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ApiHandler implements HttpHandler {

    Workspace workspace;
    WorkspaceDBClient workspaceDBClient;

    public ApiHandler(Workspace ws, WorkspaceDBClient client) {
        workspace = ws;
        workspaceDBClient = client;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            if (exchange.getRequestURI().getPath().equals("/api/userList")) {
                getUserList(exchange);
            }
        } else if (exchange.getRequestMethod().equals("POST")) {
            if (exchange.getRequestURI().getPath().equals("/api/makeTask")) {
                createNewTask(exchange);
            }
        }
    }

    private void getUserList(HttpExchange exchange) throws IOException {
        List<User> userList = workspace.getUsers();

        // FORMAT: "User [name=" + name + ", id=" + id + ", title=" + title + ", available=" + available + ", currentTasks=" + currentTasks + ", skillLevels" + skillLevels]";
        String response = workspace.getUsers().toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private void createNewTask(HttpExchange exchange) {

    }

    private void getTaskList(HttpExchange exchange) throws IOException {

        String response = workspace.getTasks().toString();
        //format: [Task, Task, Task, ...]
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
