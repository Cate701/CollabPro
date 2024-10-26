package org.collabStudios.model;

import java.util.*;
public class Workspace {
    private List<String> skills;
    private List<User> users;
    private List<Task> tasks;
    private int maxSkillLevel;

    public Workspace(List<String> skills, List<User> users, List<Task> tasks, int maxSkillLevel) {
        this.skills = skills;
        this.users = users;
        this.tasks = tasks;
        this.maxSkillLevel = maxSkillLevel;
    }

    public ArrayList<User> createTeam(Task task) {


        return null; //temp placeholder
    }

    public void addSkill(String newSkill) {
        skills.add(newSkill);
    }

    public void addUser(User newUser) {
        users.add(newUser);
    }

    public void addTasks(Task newTask) {
        tasks.add(newTask);
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getMaxSkillLevel() {
        return maxSkillLevel;
    }

    public void setMaxSkillLevel(int maxSkillLevel) {
        //maybe add code to refactor all the skill levels to be
        // proportionate to the new max level. maybe.
        this.maxSkillLevel = maxSkillLevel;
    }
}
