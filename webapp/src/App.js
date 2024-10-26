import './style.css';
import TaskBox from "./components/Task"
import Sidebar from "./components/Sidebar"
import React from "react"

export default function App() {
  return (
      <div className="container-flex">
        <Sidebar />
        <TaskBox />
      </div>
  )
}
