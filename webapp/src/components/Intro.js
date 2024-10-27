import React, { useState, useEffect } from "react";

const Intro = ({ onEnter }) => {
    const [showIntro, setShowIntro] = useState(true);

    const handleEnter = () => {
        setShowIntro(false);
        onEnter();
    };

    if (!showIntro) {
        return null;
    }

    return (
        <div id="intro-screen" className="intro-screen">
            <div id="intro-vert" className="intro-vert">
                <img src="/collabprologotransparent.png" alt="Logo" id="logo" className="logo" />
                <button id="enter-btn" className="enter-btn" onClick={handleEnter}>
                    Enter
                </button>
            </div>
        </div>
    );
};

export default Intro;