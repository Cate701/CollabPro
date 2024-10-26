package org.collabStudios.database;

import org.collabStudios.model.Workspace;

public class WorkspaceService {

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
                + " available INTEGER NOT NULL,"
        );

        var makeTasksTableBuilder = new StringBuilder(
                "CREATE TABLE IF NOT EXISTS tasks ("
                + "	taskId INTEGER PRIMARY KEY,"
                + " name TEXT NOT NULL,"
                + " duration INTEGER NOT NULL,"
                + " due TEXT NOT NULL,"
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

    }

}
