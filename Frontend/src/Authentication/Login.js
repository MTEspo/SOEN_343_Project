import React from "react";
import { Link } from "react-router-dom";

function Login() {
  return (
    <div>
      <h2>Login</h2>
      <form>
        <label>Username:</label>
        <input type="text" placeholder="Enter username" />
        <br />
        <label>Password:</label>
        <input type="password" placeholder="Enter password" />
        <br />
        <button type="submit">Login</button>
      </form>
      <p>
        If you don't have an account, <Link to="/signup">sign up here</Link>.
      </p>
    </div>
  );
}

export default Login;