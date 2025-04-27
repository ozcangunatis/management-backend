import React from "react";
import "./App.css";
import Register from "./components/register";
import HomePage from "./components/HomePage";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
<<<<<<< HEAD
import LoginPage from "./components/LoginPage";
import ChangePasswordPage from "./components/ChangePasswordPage";
import IzinOnay from "./components/IzinOnay";

const App = () => {
=======
import Register from "./components/Register";

function App() {
>>>>>>> a4bc0a72505358434125e9942f4a12047fe92f1e
  return (
    <Router>
      <div>
        <HomePage />
<<<<<<< HEAD
        <LoginPage />
        <ChangePasswordPage />

=======
>>>>>>> a4bc0a72505358434125e9942f4a12047fe92f1e
      </div>
    </Router>
  );
};

export default App;
