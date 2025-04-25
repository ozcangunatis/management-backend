import React from "react";
import "./App.css";
import Register from "./components/register";
import HomePage from "./components/HomePage";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import LoginPage from "./components/LoginPage";
import ChangePasswordPage from "./components/ChangePasswordPage";
import IzinOnay from "./components/IzinOnay";

const App = () => {
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
