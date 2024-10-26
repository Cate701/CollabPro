package org.collabStudios.model;

import java.util.*;

public class User {
    private int id;
    private String name;
    private String title;
    private boolean available;
    private HashMap<String, Integer> skillLevels;
    private List<Task> currentTasks;

    public User(String name, String title, boolean available, HashMap<String, Integer> skillLevels, List<Task> currentTasks) {
        this.name = name;
        this.title = title;
        this.available = available;
        this.skillLevels = skillLevels;
        this.currentTasks = currentTasks;
    }

    public User(int id, String name, String title) {
        this.id = id;
        this.name = name;
        this.title = title;
        available = true;
        skillLevels = new HashMap<>();
    }


    public void setSkill(String skill, int newLevel) {
        skillLevels.put(skill, newLevel);
    }

    public int getSkill(String skill) {
        return skillLevels.get(skill);
    }

    public List<Task> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(List<Task> currentTasks) {
        this.currentTasks = currentTasks;
    }

    public HashMap<String, Integer> getAllSkillLevels() {
        return skillLevels;
    }

    public void setAllSkillLevels(HashMap<String, Integer> skillLevels) {
        this.skillLevels = skillLevels;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}