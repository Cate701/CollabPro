import React, { useState, useEffect } from "react";

export default function Sidebar() {
    const [teamMembers, setTeamMembers] = useState([]);
    const [skills, setSkills] = useState([]);
    const [newMember, setNewMember] = useState("");
    const [newSkill, setNewSkill] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    const serverJSONToUser = function (serverJSON) {
        return {
            newMember : serverJSON.name,
        };
    }

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                const membersResponse = await fetch("http://localhost:8000/api/users");
                const membersData = await membersResponse.json();
                setTeamMembers(membersData.map(member => member.name));

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

        let newUser = userData.name;

        setTeamMembers([...teamMembers, newUser]);
        setNewMember("");

        closeModal();
    };

    return (
        <div className="sidebar">
            <div className="admin-panel">
                <h1>Admin Name</h1>

                <div className="skills">
                    <h4>Skills</h4>
                    {skills.map((skill, index) => (
                        <div key={index}>
                            <label htmlFor={`skill-${index}`}>{skill}</label>
                        </div>
                    ))}
                </div>
            </div>

            <div className="team-list">
                <h4>Team List</h4>
                {teamMembers.map((member, index) => (
                    <div key={index}>
                        <label htmlFor={`member-${index}`}>{member}</label>
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
                                            <span>{skill}</span>
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
        </div>
    );
}