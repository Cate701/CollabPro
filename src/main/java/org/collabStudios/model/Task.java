package org.collabStudios.model;

import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Task {

    private int id;

    //Project name
    private String name;

    //list of users assigned to this project
    private ArrayList<User> assignedUsers;

    //timespan of project
    //if set to -1, no hour estimate has been given
    private int hours;

    //Date project must be done by
    private String dueDate;

    //dictionary of desired skill level
    private Dictionary<String, Integer> desiredSkillLevel;

    //dictionary of REAL skill level
    private Dictionary<String, Integer> realSkillLevel;

    //boolean for whether or not a task is completed
    boolean completed;



    public Task(String name, Dictionary<String, Integer> desiredSkillLevel, int hours, Workspace workspace) {
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = hours;
        this.dueDate = null;
        this.realSkillLevel = new Hashtable<>();
        completed = false;
        id = workspace.getCurrTaskID();
        workspace.incrementID();
    }

    public Task(int id, String name, Dictionary<String, Integer> desiredSkillLevel, int hours, String dueDate) {
        this.id = id;
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = hours;
        this.dueDate = dueDate;
        this.realSkillLevel = new Hashtable<>();
        completed = false;
    }

    public Task(String name, Dictionary<String, Integer> desiredSkillLevel, String dueDate, Workspace workspace) {
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = -1;
        this.dueDate = dueDate;
        this.assignedUsers = new ArrayList<>();
        this.realSkillLevel = new Hashtable<>();
        completed = false;
        id = workspace.getCurrTaskID();
        workspace.incrementID();
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void addSkill(String skill) {
        desiredSkillLevel.put(skill, 0);
        realSkillLevel.put(skill, 0);
    }

    public Dictionary<String, Integer> getRealSkillLevel() {
        return realSkillLevel;
    }

    public void setRealSkillLevel(Dictionary<String, Integer> realSkillLevel) {
        this.realSkillLevel = realSkillLevel;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Dictionary<String, Integer> getDesiredSkillLevel() {
        return desiredSkillLevel;
    }

    public void setDesiredSkillLevel(Dictionary<String, Integer> desiredSkillLevel) {
        this.desiredSkillLevel = desiredSkillLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
