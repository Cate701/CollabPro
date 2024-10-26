package org.collabStudios.model;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Task {

    //Project name
    private String name;

    private Workspace workspace;

    //list of users assigned to this project
    private ArrayList<User> assignedUsers;

    //timespan of project
    //if set to -1, no hour estimate has been given
    private int hours;

    //Date project must be done by
    private LocalDateTime dueDate;

    //dictionary of desired skill level
    private Dictionary<String, Integer> desiredSkillLevel;

    //dictionary of REAL skill level
    private Dictionary<String, Integer> realSkillLevel;

    public Task(String name, Dictionary<String, Integer> desiredSkillLevel, int hours, Workspace workspace) {
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = hours;
        this.dueDate = null;
        this.realSkillLevel = new Hashtable<>();
        this.workspace = workspace;
        autoAssignUsers();
    }

    public Task(String name, Dictionary<String, Integer> desiredSkillLevel, int hours, LocalDateTime dueDate, Workspace workspace) {
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = hours;
        this.dueDate = dueDate;
        this.realSkillLevel = new Hashtable<>();
        this.workspace = workspace;
        autoAssignUsers();
    }

    public Task(String name, Dictionary<String, Integer> desiredSkillLevel, LocalDateTime dueDate, Workspace workspace) {
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = -1;
        this.dueDate = dueDate;
        this.assignedUsers = new ArrayList<>();
        this.realSkillLevel = new Hashtable<>();
        autoAssignUsers();
    }

    public void autoAssignUsers() {
        assignedUsers = workspace.assignTask(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(ArrayList<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Dictionary<String, Integer> getDesiredSkillLevel() {
        return desiredSkillLevel;
    }

    public void setDesiredSkillLevel(Dictionary<String, Integer> desiredSkillLevel) {
        this.desiredSkillLevel = desiredSkillLevel;
    }
}
