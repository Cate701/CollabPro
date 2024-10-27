import './style.css';
import TaskBox from "./components/Task"
import Sidebar from "./components/Sidebar"
import Intro from "./components/Intro"
import React, {useState} from "react"

const App = () => {
  const [showIntro, setShowIntro] = useState(true);

  const handleEnter = () => {
    setShowIntro(false);
  };

  return (
    <div className="App">
      {showIntro ? (
        <Intro onEnter={handleEnter} />
      ) : (
        <div className="container-flex">
          <Sidebar />
          <TaskBox />
        </div>
      )}
    </div>
  );
};

export default App;
