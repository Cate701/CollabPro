package org.collabStudios.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.collabStudios.database.WorkspaceDBClient;
import org.collabStudios.model.Task;
import org.collabStudios.model.User;
import org.collabStudios.model.Workspace;
import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class ApiHandler implements HttpHandler {

    Workspace workspace;
    WorkspaceDBClient workspaceDBClient;

    public ApiHandler(Workspace ws, WorkspaceDBClient client) {
        workspace = ws;
        workspaceDBClient = client;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (exchange.getRequestMethod().equals("GET")) {
            if (exchange.getRequestURI().getPath().equals("/api/users")) {
                getUserList(exchange);
                return;
            } else if (exchange.getRequestURI().getPath().equals("/api/tasks")) {
                getTaskList(exchange);
                return;
            } else if (exchange.getRequestURI().getPath().equals("/api/skills")) {
                getSkillList(exchange);
                return;
            }
        } else if (exchange.getRequestMethod().equals("POST")) {
            if (exchange.getRequestURI().getPath().equals("/api/makeTask")) {
                createNewTask(exchange);
                return;
            }
            else if (exchange.getRequestURI().getPath().equals("/api/makeUser")) {
                createNewUser(exchange);
                return;
            }
        }

        String response = "404 Not Found\n";
        exchange.sendResponseHeaders(404, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void getUserList(HttpExchange exchange) throws IOException {
        List<User> userList = workspace.getUsers();

        JSONArray responseJSON = new JSONArray(userList);
        String response = responseJSON.toString();

        // FORMAT: "User [name=" + name + ", id=" + id + ", title=" + title + ", available=" + available + ", currentTasks=" + currentTasks + ", skillLevels" + skillLevels]";
//        String response = workspace.getUsers().toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private void createNewTask(HttpExchange exchange) {
        String queryInfo = exchange.getRequestURI().getQuery(); //exclude question mark
        //Format: name="name"&date="date"&DesiredSkill="a,b,c,d,e,..."
        String[] info = queryInfo.split("&");

        //get name
        String[] nameInfo = info[0].split("\"");
        String name = nameInfo[1];

        //get date
        String[] dateInfo = info[1].split("\"");
        String date = dateInfo[1];

        //get skill hashMap
        String[] skillLevels = info[2].split("\"")[1].split(",");
        List<String> skillNames = workspace.getSkills();
        Dictionary<String, Integer> desiredSkills = new Hashtable<>();
        for (int i = 0; i < skillNames.size(); i++) {
            String currSkill = skillNames.get(i);
            int currLevel = Integer.parseInt(skillLevels[i]);
            desiredSkills.put(currSkill, currLevel);
        }

        Task newTask = new Task(name, desiredSkills, date, workspace);
        workspace.addTasks(newTask);
    }

    public void createNewUser(HttpExchange exchange) {
        String queryInfo = exchange.getRequestURI().getQuery(); //exclude question mark
        //Format: name="name"&skillLevels="a,b,c,d,e,..."&title="title"
        String[] info = queryInfo.split("&");

        //get name info
        String[] nameInfo = info[0].split("\"");
        String name = nameInfo[1];

        //get skill hashMap
        String[] skillLevels = info[1].split("\"")[1].split(",");
        List<String> skillNames = workspace.getSkills();
        HashMap<String, Integer> skillLevelsDict = new HashMap<>();
        for (int i = 0; i < skillNames.size(); i++) {
            String currSkill = skillNames.get(i);
            int currLevel = Integer.parseInt(skillLevels[i]);
            skillLevelsDict.put(currSkill, currLevel);
        }

        //get title
        String[] titleInfo = info[2].split("\"");
        String title = titleInfo[1];

        User user = new User(name, title, true, skillLevelsDict, new ArrayList<>());
        workspace.addUser(user);
    }

    private void getTaskList(HttpExchange exchange) throws IOException {
        JSONArray responseJSON = new JSONArray(workspace.getTasks());
        String response = responseJSON.toString();

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private void getSkillList(HttpExchange exchange) throws IOException {
        JSONArray responseJSON = new JSONArray(workspace.getSkills());
        String response = responseJSON.toString();

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
