package org.collabStudios.model;

import java.util.*;
import java.time.LocalDateTime;

public class Task {

    //Project name
    String name;

    //list of users assigned to this project
    ArrayList<User> assignedUsers;

    //timespan of project
    //if set to -1, no hour estimate has been given
    int hours;

    //Date project must be done by
    LocalDateTime dueDate;

    //dictionary of desired skill level
    HashMap<String, Integer> desiredSkillLevel;

    //dictionary of REAL skill level
    HashMap<String, Integer> realSkillLevel;

    public Task(String name, HashMap<String, Integer> desiredSkillLevel, int hours) {
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = hours;
        this.dueDate = null;
        this.assignedUsers = new ArrayList<>();
        this.realSkillLevel = new HashMap<>();
    }

    public Task(String name, HashMap<String, Integer> desiredSkillLevel, int hours, LocalDateTime dueDate) {
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = hours;
        this.dueDate = dueDate;
        this.assignedUsers = new ArrayList<>();
        this.realSkillLevel = new HashMap<>();
    }

    public Task(String name, HashMap<String, Integer> desiredSkillLevel, LocalDateTime dueDate) {
        this.name = name;
        this.desiredSkillLevel = desiredSkillLevel;
        this.hours = -1;
        this.dueDate = dueDate;
        this.assignedUsers = new ArrayList<>();
        this.realSkillLevel = new HashMap<>();
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

    public HashMap<String, Integer> getDesiredSkillLevel() {
        return desiredSkillLevel;
    }

    public void setDesiredSkillLevel(HashMap<String, Integer> desiredSkillLevel) {
        this.desiredSkillLevel = desiredSkillLevel;
    }

    public List<String> getNamesOfAssignedUsers() {
        List<String> names = new ArrayList<>();
        for (User user : assignedUsers) {
            names.add(user.getName());
        }
        return names;
    }

}
