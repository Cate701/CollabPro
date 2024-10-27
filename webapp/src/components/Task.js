import React, { useState, useEffect } from "react";

export default function TaskBox() {
    const [tasks, setTasks] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [taskName, setTaskName] = useState("");
    const [skills, setSkills] = useState([]);
    const [dueDate, setDueDate] = useState("");
    const [availableSkills, setAvailableSkills] = useState([]);
    
    const serverJSONToTask = function (serverJSON) {
        return {
            taskName : serverJSON.name,
            skill : serverJSON.skill,
            dueDate : serverJSON.dueDate,
            memberNames : serverJSON.assignedUsers.map(u => u.name),
            skills: serverJSON.desiredSkillLevel
        };
    }

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                const skFetch = await fetch("http://localhost:8000/api/skills");
                const taskResponse = await fetch("http://localhost:8000/api/tasks", {
                    method: "GET",
                    mode: 'cors',
                    headers: {
                    'Access-Control-Allow-Origin':'*'
                    }
                });
                const taskData = await taskResponse.json();
                const skData = await skFetch.json();
                setAvailableSkills(skData);
                setSkills(skData.map(s => 0));

                setTasks(taskData.map(serverJSONToTask));

            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };

        fetchInitialData();
    }, []);

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    async function handleAddTask() {

        const params = new URLSearchParams({
            name: taskName,
            dueDate: dueDate,
            skills: skills.toString(),
        });

        const query = "http://localhost:8000/api/task?" + params.toString();

        const taskResponse = await fetch(query, {
            method: "POST",
            mode: 'cors',
            headers: {
              'Access-Control-Allow-Origin':'*'
            }
        });
        const taskData = await taskResponse.json();

        let newTask = serverJSONToTask(taskData);

        setTasks([...tasks, newTask]);
        setTaskName("");
        closeModal();
    }

    return (
        <main>
            <div className="title">Tasks</div>
            <div className="container">
                <div className="button-container">
                    <button className="create-button" onClick={openModal}>
                        Create New Task
                    </button>
                </div>
                <div id="task-container">
                    {tasks.map((task, index) => (
                        <div className="task-box" key={index}>
                            <div className="task-header">
                                <span>{task.taskName}</span>
                                <span>{task.dueDate}</span>
                            </div>
                            <div className="members">Members: {task.memberNames.join(", ")}</div>
                            <div className="skills">Skills: {Object.keys(task.skills).map(key => key + ": " + task.skills[key]).join(", ")}</div>
                        </div>
                    ))}
                </div>
            </div>

            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal">
                        <h2>Create New Task</h2>
                        <div className="form-group">
                            <label>Task Name</label>
                            <input
                                type="text"
                                value={taskName}
                                onChange={(e) => setTaskName(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Skills Required</label>
                            {availableSkills.map((skill, index) => (
                                <div className="skill-header">
                                    <div className = "skill">
                                        <span>{skill}</span>
                                    </div>
                                    <div className = "slider">
                                        <div class="slidecontainer">
                                            <input type="range" min="0" max="5" value={skills[index]} 
                                            onChange={(e)=> {
                                                skills[index] = e.target.value;
                                                setSkills([...skills]);}} class="slider" id="myRange" />
                                        </div>
                                    </div>
                                </div>

                            ))}
                        </div>
                        <div className="form-group">
                            <label>Due Date</label>
                            <input
                                type="date"
                                value={dueDate}
                                onChange={(e) => setDueDate(e.target.value)}
                            />
                        </div>
                        <div className="modal-buttons">
                            <button onClick={handleAddTask}>Add Task</button>
                            <button onClick={closeModal}>Cancel</button>
                        </div>
                    </div>
                </div>
            )}
        </main>
    );
}
