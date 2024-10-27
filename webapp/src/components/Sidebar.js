import React, { useState, useEffect } from "react";

export default function Sidebar() {
    const [teamMembers, setTeamMembers] = useState([]);
    const [skills, setSkills] = useState([]);
    const [newMember, setNewMember] = useState("");
    const [newSkill, setNewSkill] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const [newTotalSkill, setTotalSkill] = useState("");

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    const serverJSONToUser = function (serverJSON) {
        return {
            name : serverJSON.name,
            skills: serverJSON.allSkillLevels,
        };
    }

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                const membersResponse = await fetch("http://localhost:8000/api/users");
                const membersData = await membersResponse.json();
                setTeamMembers(membersData.map(serverJSONToUser));

                const skillsResponse = await fetch("http://localhost:8000/api/skills");
                const skillsData = await skillsResponse.json();
                setSkills(skillsData);
                setNewSkill(skillsData.map(s => 0));
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };

        fetchInitialData();
    }, []);

    async function handleAddMember(){
        const params = new URLSearchParams({
            name: newMember,
            skills: newSkill.toString(),
            title: "bs"
        });

        const query = "http://localhost:8000/api/user?" + params.toString();

        const userResponse = await fetch(query, {
            method: "POST",
            mode: 'cors',
            headers: {
              'Access-Control-Allow-Origin':'*'
            }
        });
        const userData = await userResponse.json();

        let newUser = serverJSONToUser(userData);

        setTeamMembers([...teamMembers, newUser]);
        setNewMember("");

        closeModal();
    };

    async function handleAddSkill(){
        const params = new URLSearchParams({
            skill: newTotalSkill
        });

        const query = "http://localhost:8000/api/skill?" + params.toString();

        const skillResponse = await fetch(query, {
            method: "POST",
            mode: 'cors',
            headers: {
              'Access-Control-Allow-Origin':'*'
            }
        });
        const skillData = await skillResponse.json();

        let nSkill = serverJSONToUser(skillData);

        setSkills([...skills, nSkill]);
        setTotalSkill("");
    }

    return (
        <div className="sidebar">
            <div class="biglogobg">
                <img src="/collabprologotransparent.png" class="biglogo"/>
            </div>
            <div className="admin-panel">
                <div className="skills">
                    <h4>Team Skills</h4>
                    {skills.map((skill, index) => (
                        <div key={index}>
                            <label htmlFor={`skill-${index}`}>{skill}</label>
                        </div>
                    ))}
                    <div className="form-group">
                        <input
                            type="text"
                            placeholder ="Add New Skill"
                            value={newTotalSkill}
                            onChange={(e) => setTotalSkill(e.target.value)}
                        />
                        </div>
                    <div className="add-skill">
                        <button onClick = {handleAddSkill}>Add Skill</button>
                    </div>
                </div>
            </div>
            

            <div className="team-list">
                <h4>Team List</h4>
                {teamMembers.map((member, index) => (
                    <div key={index}>
                        <label htmlFor={`member-${index}`}>{member.name}</label>
                        <ul class='user-skill-list'>
                            {Object.keys(member.skills).map(k => (
                                <li class='user-skill-elem'>{k}: {member.skills[k]}</li>
                            ))}
                        </ul>
                    </div>
                ))}
                <div className="add-member">
                    <button onClick={openModal}>Add Team Member</button>
                </div>

                {isModalOpen && (
                    <div className="modal-overlay">
                        <div className="modal">
                            <h2>Add New Team Member</h2>
                            <div className="form-group">
                                <label>Member Name</label>
                                <input
                                    type="text"
                                    value={newMember}
                                    onChange={(e) => setNewMember(e.target.value)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Skills</label>
                                {skills.map((skill, index) => (
                                    <div className="skill-header">
                                        <div className = "skill-member">
                                            <span>{skill} - {newSkill[index]}</span>
                                        </div>
                                        <div className = "slider">
                                            <div class="slidecontainer">
                                                <input type="range" min="0" max="5" value={newSkill[index]}  
                                                onChange={(e)=> {
                                                    newSkill[index] = e.target.value;
                                                    setNewSkill([...newSkill]);}} class="slider" id="myRange"/>
                                            </div>
                                        </div>
                                    </div>

                                ))}
                            </div>
                            <div className="modal-buttons">
                                <button onClick={handleAddMember}>Add Member</button>
                                <button onClick={closeModal}>Cancel</button>
                            </div>
                        </div>
                    </div>
                )}
            </div>

            <div className="admin-view-disclosure">
                <p>Admin View</p>
            </div>
        </div>
    );
}