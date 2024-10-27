package org.collabStudios.model;

import java.lang.reflect.Array;
import java.util.*;
public class Workspace {
    private String name;
    private int maxSkillLevel;
    private List<String> skills;
    private List<User> users;
    private List<Task> tasks;

    public Workspace(String name, List<String> skills, List<User> users, List<Task> tasks, int maxSkillLevel) {
        this.skills = skills;
        this.users = users;
        this.tasks = tasks;
        this.maxSkillLevel = maxSkillLevel;
        this.name = name;
    }

    public ArrayList<User> searchNameAndSkill(String name, HashMap<String, Integer> skill) {
        ArrayList<User> nameMatches = searchName(name);
        ArrayList<User> skillMatches = searchSkills(skill);
        //now compare and return users that were in BOTH lists
        ArrayList<User> results = new ArrayList<>();
        for (User user : nameMatches) {
            if (skillMatches.contains(user)) {
                results.add(user);
            }
        }
        return results;
    }

    public ArrayList<User> searchName(String name) {
        name = name.toLowerCase();
        ArrayList<User> results = new ArrayList<>();
        for (User user : users) {
            String userName = user.getName().toLowerCase();
            if (userName.contains(name)) {
                results.add(user);
            }
        }
        return results;
    }

    /*  searchSkills() function will take in a HashMap of wanted skill levels
        will return a list of users whose skill levels are at or above the desired levels
    */
    public ArrayList<User> searchSkills(HashMap<String, Integer> wantedSkills) {
        //this is to avoid accidentally changing the original users Arraylist
        ArrayList<User> results = new ArrayList<>(users);

        //this loop goes through eat desired skill and removes users whose skill level is below the threshold
        for (String skill : skills) {
            int wantedLevel = wantedSkills.get(skill);
            results.removeIf(user -> user.getSkill(skill) < wantedLevel);
        }

        return results;
    }

    public ArrayList<User> assignTask(Task task) {
        ArrayList<User> assignedUsers = new ArrayList<>();
        int realSkillLevel = 0;
        //essentially makes array of all keys in the hashmap. dw abt it.
        Enumeration keys = task.getDesiredSkillLevel().keys();
        //iterates through keys
        while (keys.hasMoreElements()) {
            String skill = (String) keys.nextElement();
            int desiredLevel = task.getDesiredSkillLevel().get(skill);
            if (desiredLevel <= maxSkillLevel) {
                User u = individualAssignment(skill, desiredLevel);
                if (assignedUsers.contains(u)) {
                    assignedUsers.add(u);
                }
            } else {
                ArrayList<User> userRecs  = bigTeamAssignment(skill, desiredLevel);
                for (User u : userRecs) {
                    if (assignedUsers.contains(u)) {
                        assignedUsers.add(u);
                    }
                }
            } for (User u : assignedUsers) {
                realSkillLevel += u.getSkill(skill);
            }
        }
        return assignedUsers;
    }

    public ArrayList<User> bigTeamAssignment(String skill, int desiredLevel) {
        ArrayList<User> usersAdding = null;
        int totalLevel = 0;
        while (totalLevel < desiredLevel) {
            int teamSkill = users.get(0).getSkill(skill);
            User highestSkill = users.get(0);
            //iterate through all users, comparing their skill levels. adds high-skill users
            //until they meet skill requirement.
            for (int i = 1; i < users.size(); i ++) {
                User u = users.get(i);
                //adds up the full skill level of the team
                teamSkill += u.getSkill(skill);
                if (u.isAvailable()) {
                    if (u.getSkill(skill) > highestSkill.getSkill(skill) && !usersAdding.contains(u)) {
                        highestSkill = u;
                    }
                }
            }
            usersAdding.add(highestSkill);
            totalLevel += highestSkill.getSkill(skill);
            //if the full team's skill is less than desired level, lower desired level.
            //will add all users with skill to usersAdding.
            if (teamSkill < desiredLevel) {
                desiredLevel = teamSkill;
            }
        }
        return usersAdding;
    }

    public User individualAssignment(String skill, int desiredLevel) {
        User current = users.get(0);
        for (User u : users) {
            if (u.isAvailable()) {
                if (u.getSkill(skill) == desiredLevel) {
                    return u;
                }
                // compares the differences between desired skill level and user skill. tries to find one closest to 0.
                else if (Math.abs(desiredLevel - u.getSkill(skill)) < Math.abs(desiredLevel - current.getSkill(skill))) {
                    current = u;
                } //is this fucking correct. who knows.
            }
        }
        return current;
    }

    //Will return a list of the assigned users
    //Will assign realSkillLevels to task manually
    public ArrayList<User> assignTeamComplex(Task task) {
        Dictionary<String, Integer> desiredSkills = task.getDesiredSkillLevel();
        ArrayList<User> assignedUsers = new ArrayList<>();
        Dictionary<String, Integer> currSkillLevels = new Hashtable<>();
        currSkillLevels = initEmptySkills(currSkillLevels, skills); //init skill levels to zero
        for (String skill : skills) { //for every skill
            int neededSkill = desiredSkills.get(skill) - currSkillLevels.get(skill);
            if (neededSkill > 0) { //if we need more skill
                boolean needMoreSkills = true;
                while(needMoreSkills) { //This while loop will run as long as we need more people AND more people are available
                    User newUser; //initially null
                    if (neededSkill > maxSkillLevel) {
                        newUser = getUserForSkillLevel(skill, maxSkillLevel);
                    } else {
                        newUser = getUserForSkillLevel(skill, neededSkill);
                    }
                    if (newUser == null) { //if no possible user was found
                        needMoreSkills = false;
                    }
                    else {
                        assignedUsers.add(newUser); //add to assigned list
                        currSkillLevels = updateSkillLevels(assignedUsers, currSkillLevels); //update skill levels
                        neededSkill = desiredSkills.get(skill) - currSkillLevels.get(skill);
                    }
                }

            }
        }
        task.setRealSkillLevel(currSkillLevels);
        return assignedUsers;
    }

    //will find a user that is available AND most closely matches the given skillLevel
    public User getUserForSkillLevel(String skill, int skillLevel) {
        User currUser = null;
        for (User user : users) { //preemptively assign first available user
            if (user.isAvailable()) {
                currUser = user;
                break;
            }
        }
        if (currUser == null) {
            return null;
        }
        int currAbsDistance = Math.abs(currUser.getSkill(skill) - skillLevel);
        for (User user : users) { //go through and find best match to skill level
            if (currUser.getSkill(skill) == skillLevel) { //if we have perfect match, return
                currUser.setAvailable(false);
                return currUser;
            }
            else {
                int absDistance = Math.abs(user.getSkill(skill) - skillLevel);
                if (absDistance < currAbsDistance) {
                    currAbsDistance = absDistance;
                    currUser = user;
                }
            }
        }
        return currUser;
    }

    public Dictionary<String, Integer> updateSkillLevels(ArrayList<User> users, Dictionary<String, Integer> skillLevels) {
        for (User user : users) {
            for (String skill : skills) {
                int newLevel = skillLevels.get(skill) + user.getSkill(skill);
                skillLevels.put(skill, newLevel);
            }
        }
        return skillLevels;
    }

    //will create dictionary of all skills with levels set to zero
    public Dictionary<String, Integer> initEmptySkills(Dictionary<String, Integer> skillLevels, List<String> skills) {
        for (String skill : skills) {
            skillLevels.put(skill, 0);
        }
        return skillLevels;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
