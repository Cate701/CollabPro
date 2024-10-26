package org.collabStudios;

import org.collabStudios.database.WorkspaceDBClient;
import org.collabStudios.database.WorkspaceService;
import org.collabStudios.model.Workspace;

import java.util.List;

public class Main {

    private static void sqlTest() {
        WorkspaceDBClient client = new WorkspaceDBClient("testWs");
        System.out.println("Connection");
        Workspace dummyNew = new Workspace("testWs", List.of(new String[]{"frontend", "backend", "database"}), null, null, 5);
        WorkspaceService.setupNewWorkspaceBlank(client,dummyNew);
        client.close();
    }

    public static void main(String[] args) {
        sqlTest();
    }

}