import React from "react";
import "./App.css";

import HomePage from "./components/HomePage";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

function App() {
  return (
    <Router>
      <div>
        <HomePage />
      </div>
    </Router>
  );
};

export default App;
