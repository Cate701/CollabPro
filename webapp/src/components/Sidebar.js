import React, { useState, useEffect } from "react";

export default function Sidebar() {
    const [teamMembers, setTeamMembers] = useState([]);
    const [skills, setSkills] = useState([]);
    const [newMember, setNewMember] = useState("");
    const [newSkill, setNewSkill] = useState("");

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                const membersResponse = await fetch("https://jsonplaceholder.typicode.com/users");
                const membersData = await membersResponse.json();
                setTeamMembers(membersData.map(member => member.name));

                const skillsResponse = await fetch("");
                const skillsData = await skillsResponse.json();
                setSkills(skillsData.slice(0, 5).map(skill => skill.title));
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };

        fetchInitialData();
    }, []);

    const handleAddMember = () => {
        if (newMember.trim()) {
            setTeamMembers([...teamMembers, newMember]);
            setNewMember("");
        }
    };

    const handleAddSkill = () => {
        if (newSkill.trim()) {
            setSkills([...skills, newSkill]);
            setNewSkill("");
        }
    };

    return (
        <div className="sidebar">
            <div className="admin-panel">
                <h1>Admin Name</h1>

                <div className="skills">
                    <h4>Skills</h4>
                    {skills.map((skill, index) => (
                        <div key={index}>
                            <input type="checkbox" id={`skill-${index}`} />
                            <label htmlFor={`skill-${index}`}>{skill}</label>
                        </div>
                    ))}
                    <div className="add-skill">
                        <input
                            type="text"
                            placeholder="Add new skill"
                            value={newSkill}
                            onChange={(e) => setNewSkill(e.target.value)}
                        />
                        <button onClick={handleAddSkill}>Add Skill</button>
                    </div>
                </div>
            </div>

            <div className="team-list">
                <h4>Team List</h4>
                {teamMembers.map((member, index) => (
                    <div key={index}>
                        <input type="checkbox" id={`member-${index}`} />
                        <label htmlFor={`member-${index}`}>{member}</label>
                    </div>
                ))}
                <div className="add-member">
                    <input
                        type="text"
                        placeholder="Add new member"
                        value={newMember}
                        onChange={(e) => setNewMember(e.target.value)}
                    />
                    <button onClick={handleAddMember}>Add Team Member</button>
                </div>
            </div>
        </div>
    );
}