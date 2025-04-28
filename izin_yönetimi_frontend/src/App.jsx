import React from "react";
import "./App.css";
import Register from "./components/register";
import HomePage from "./components/HomePage";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

function App() {
  return (
    <Router>
      <div>
        <HomePage />

        <LoginPage />
        <ChangePasswordPage />
      </div>
    </Router>
  );
};

export default App;
