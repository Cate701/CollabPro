package org.collabStudios.database;

import org.collabStudios.model.Task;
import org.collabStudios.model.User;
import org.collabStudios.model.Workspace;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;

public class WorkspaceService {

    public static Workspace loadWorkspace(WorkspaceDBClient client) {
        Connection conn = client.getConnection();

        int maxSkill;
        ArrayList<String> skills = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Task> tasks = new ArrayList<>();
        String name;

        var propsSql = "SELECT maxSkillLevel, name FROM properties";
        var skillsSql = "SELECT name FROM skills";
        var usersSql = "SELECT * FROM users";
        var tasksSql = "SELECT * FROM tasks";
        var asnFmtSql = "SELECT userId FROM assignments WHERE taskId = %d";

        System.out.println("Loading workspace...");

        try {

            // Basic properties

            Statement propsStmt = conn.createStatement();
            ResultSet propsResult = propsStmt.executeQuery(propsSql);

            maxSkill = propsResult.getInt(1);
            name = propsResult.getString(2);

            // Skills

            Statement skillsStmt = conn.createStatement();
            ResultSet skillsResult = skillsStmt.executeQuery(skillsSql);

            while (skillsResult.next()) {
                skills.add(skillsResult.getString(1));
            }


            // Users:

            Statement usersStmt = conn.createStatement();
            ResultSet usersResult = usersStmt.executeQuery(usersSql);

            while (usersResult.next()) {
                User newU = new User(
                        usersResult.getInt("userId"),
                        usersResult.getString("first") + " " + usersResult.getString("last"),
                        usersResult.getString("title")
                );

                boolean available = usersResult.getBoolean("available");
                newU.setAvailable(available);

                HashMap<String, Integer> userSkills = new HashMap<>();
                int firstSkillCol = 6;
                for (int i = firstSkillCol; i < firstSkillCol + skills.size(); i++) {
                    // System.out.println(usersResult.getInt(i));
                    userSkills.put(skills.get(i - firstSkillCol), usersResult.getInt(i));
                }

                newU.setAllSkillLevels(userSkills);
                users.add(newU);
            }

            // Tasks:

            Statement tasksStmt = conn.createStatement();
            ResultSet tasksResult = tasksStmt.executeQuery(tasksSql);

            while (tasksResult.next()) {

                Dictionary<String, Integer> desiredSkills = new Hashtable<String, Integer>();
                int firstSkillCol = 6;
                for (int i = firstSkillCol; i < firstSkillCol + skills.size(); i++) {
                    // System.out.println(tasksResult.getInt(i));
                    desiredSkills.put(skills.get(i - firstSkillCol), tasksResult.getInt(i));
                }

                Task newT = new Task(
                        tasksResult.getInt("taskId"),
                        tasksResult.getString("name"),
                        desiredSkills,
                        tasksResult.getInt("duration"),
                        LocalDateTime.parse(tasksResult.getString("due"), DateTimeFormatter.ISO_DATE_TIME)
                );

                Statement assignmentsStmt = conn.createStatement();
                ResultSet assignmentsResult =
                        assignmentsStmt.executeQuery(String.format(asnFmtSql, newT.getId()));

                List<User> assignedUsers = new LinkedList<>();
                while (assignmentsResult.next()) {
                    int userId = assignmentsResult.getInt("userId");
                    var usr =  users.stream().filter(u -> {
                        return u.getId() == userId;
                    }).findFirst();

                    if (usr.isPresent()) {
                        assignedUsers.add(usr.get());
                    } else {
                        System.out.println("User " + userId + " not found");
                    }
                }

                newT.getAssignedUsers().addAll(assignedUsers);

                tasks.add(newT);

            }

        } catch (SQLException e) {
            System.out.println("Error loading workspace: " + e.getMessage());
            return null;
        }

        return new Workspace(name, skills, users, tasks, maxSkill);
    }

    /** Sets up a new database, filling in only the properties and skills tables.
     * Users and Tasks are not added.
     * @param client
     * @param workspace
     */
    public static void setupNewWorkspaceBlank(WorkspaceDBClient client, Workspace workspace) {

        var makePropertiesTable =
                "CREATE TABLE IF NOT EXISTS properties ("
                + "	maxSkillLevel INTEGER NOT NULL,"
                + " name TEXT NOT NULL"
                + ");";

        var makeSkillsTable =
                "CREATE TABLE IF NOT EXISTS skills ("
                        + "	id INTEGER PRIMARY KEY,"
                        + " name TEXT NOT NULL"
                        + ");";

        var makeUsersTableBuilder = new StringBuilder(
                "CREATE TABLE IF NOT EXISTS users ("
                + "	userId INTEGER PRIMARY KEY,"
                + " first TEXT NOT NULL,"
                + " last TEXT NOT NULL,"
                + " title TEXT NOT NULL,"
                + " available INTEGER NOT NULL,"
        );

        var makeTasksTableBuilder = new StringBuilder(
                "CREATE TABLE IF NOT EXISTS tasks ("
                + "	taskId INTEGER PRIMARY KEY,"
                + " name TEXT NOT NULL,"
                + " duration INTEGER NOT NULL,"
                + " due TEXT NOT NULL,"
                + " completed INTEGER NOT NULL,"
        );

        var makeAssignmentsTable =
                "CREATE TABLE IF NOT EXISTS assignments ("
                + "	id INTEGER PRIMARY KEY,"
                + " taskId INTEGER NOT NULL,"
                + " userId INTEGER NOT NULL"
                + ");";

        int i = 0;
        for (String skill : workspace.getSkills()) {
            makeUsersTableBuilder.append(" Skill_").append(skill).append(" INTEGER NOT NULL,");
            makeTasksTableBuilder.append(" Skill_").append(skill).append(" INTEGER NOT NULL,");
            i++;
        }
        makeUsersTableBuilder.deleteCharAt(makeUsersTableBuilder.length()-1).append(");");
        makeTasksTableBuilder.deleteCharAt(makeTasksTableBuilder.length()-1).append(");");

        var makeUsersTable = makeUsersTableBuilder.toString();
        var makeTasksTable = makeTasksTableBuilder.toString();

        // TODO: Error handling
        System.out.println("Props:");
        client.executeWriteQuery(makePropertiesTable);
        System.out.println("Skill:");
        client.executeWriteQuery(makeSkillsTable);
        System.out.println("Users:");
        client.executeWriteQuery(makeUsersTable);
        System.out.println("Tasks:");
        client.executeWriteQuery(makeTasksTable);
        System.out.println("Assignments:");
        client.executeWriteQuery(makeAssignmentsTable);

        var updatePropertiesSql =
            "INSERT INTO properties (maxSkillLevel, name)"
            + "VALUES"
            + "( "+workspace.getMaxSkillLevel()+",\""+workspace.getName()+"\");";
        System.out.println("Set props:");
        client.executeWriteQuery(updatePropertiesSql);

        StringBuilder updateSkillsSql = new StringBuilder("INSERT INTO skills (name) VALUES");
        for (String skill : workspace.getSkills()) {
            updateSkillsSql.append(" (\"").append(skill).append("\"),");
        }
        updateSkillsSql.deleteCharAt(updateSkillsSql.length()-1).append(";");
        // System.out.println(updateSkillsSql.toString());

        System.out.println("Set skills:");
        client.executeWriteQuery(updateSkillsSql.toString());

    }

}
