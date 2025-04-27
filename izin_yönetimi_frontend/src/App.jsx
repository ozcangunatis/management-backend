import React from "react";
import ChangePasswordPage from "./components/ChangePasswordPage";
import LoginPage from "./components/LoginPage";
import "./index.css";
import HomePage from "./components/HomePage";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Register from "./components/Register";

function App() {
  return (
    <Router>
      <div>
        <HomePage />
        <LoginPage />
      </div>
    </Router>
  );
}

export default App;
