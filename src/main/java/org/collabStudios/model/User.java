package org.collabStudios.model;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class User {
    private String name;
    private String title;
    private boolean available;
    private Dictionary<String, Integer> skillLevels;
    private List<Task> currentTasks;

    public User(String name, String title, boolean available, Dictionary<String, Integer> skillLevels, List<Task> currentTasks) {
        this.name = name;
        this.title = title;
        this.available = available;
        this.skillLevels = skillLevels;
        this.currentTasks = currentTasks;
    }

    public User(String name, String title) {
        this.name = name;
        this.title = title;
        available = true;
        skillLevels = new Hashtable<>();
    }

    public void setSkill(String skill, int newLevel) {
        skillLevels.put(skill, newLevel);
    }

    public int getSkill(String skill, int newLevel) {
        return skillLevels.get(skill);
    }

    public List<Task> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(List<Task> currentTasks) {
        this.currentTasks = currentTasks;
    }

    public Dictionary<String, Integer> getAllSkillLevels() {
        return skillLevels;
    }

    public void setAllSkillLevels(Dictionary<String, Integer> skillLevels) {
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
}