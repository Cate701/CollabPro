package org.collabStudios.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkspaceDBClient {

    Connection conn = null;
    String workspaceName;

    public WorkspaceDBClient(String workspaceName) {

        Path dbDir = Paths.get("db/");
        try {
            Files.createDirectories(dbDir);
        } catch (IOException e) {
            System.out.println("Error creating database directory");
            return;
        }

        String url = "jdbc:sqlite:db/"+workspaceName+".db";

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Executes a write query where we do not care about the returned data type
     * @param query
     */
    public void executeWriteQuery(String query) {
        if (conn == null) {
            return;
        }

        try {
            Statement stmt = conn.createStatement();
            boolean good = stmt.execute(query);
            System.out.println("Query executed: " + good);
            if (good) {
                System.out.println(stmt.getResultSet());
            } else {
                System.out.println(stmt.getUpdateCount());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public Connection getConnection() {
        return conn;
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Static methods:
    static boolean wsExists(String workspaceName) {
        // TODO: Check for path to ./db/workspaceName.db file
        return false;
    }

}
