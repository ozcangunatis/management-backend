import React from "react";
import ChangePasswordPage from "./components/ChangePasswordPage";
import LoginPage from "./components/LoginPage";
import "./index.css";
import HomePage from "./components/HomePage";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Register from "./components/LoginPage";
function App() {
  return (
    <Router>
      <div>
        <HomePage />
        <ChangePasswordPage />
        <LoginPage />
        <Register />
      </div>
    </Router>
  );
}

export default App;
