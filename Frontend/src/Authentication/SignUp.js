import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const roles = [
  { value: 'ATTENDEE', label: 'Attendee' },
  { value: 'SPEAKER', label: 'Speaker' },
  { value: 'ORGANIZER', label: 'Organizer' },
];

const SignUp = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('');
  const [profession, setProfession] = useState('');
  const [university, setUniversity] = useState('');
  const [organization, setOrganization] = useState('');
  const [expertise, setExpertise] = useState('');
  const [error, setError] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = {
      username,
      email,
      password,
      role: role.toUpperCase(), 
    };
    if (role === 'ATTENDEE') {
      data.profession = profession;
      data.university = university;
    } else if (role === 'SPEAKER') {
      data.expertise = expertise;
    } else if (role === 'ORGANIZER') {
      data.organization = organization;
    }
    try {
      const response = await axios.post('http://localhost:8080/api/auth/signup', data);
      console.log(response)
      navigate('/verify-email', { state: { email: email } });
    } catch (error) {
      if (error.response) {
        console.error('Error response:', error.response);
        setError(`Error ${error.response.status}: ${error.response.statusText}`);
      } else if (error.request) {
        console.error('Error request:', error.request);
        setError('Error: Network error');
      } else {
        console.error('Error:', error.message);
        setError(error.message);
      }
    }
  };

  return (
    <div>
      <h1>Sign Up</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Username:
          <input
            type="text"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
          />
        </label>
        <br />
        <label>
          Email:
          <input
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />
        </label>
        <br />
        <label>
          Password:
          <input
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />
        </label>
        <br />
        <label>
          Role:
          <select value={role} onChange={(event) => setRole(event.target.value)}>
            <option value="">Select a role</option>
            {roles.map((role) => (
              <option key={role.value} value={role.value}>
                {role.label}
              </option>
            ))}
          </select>
        </label>
        {role === 'ATTENDEE' && (
          <>
            <br />
            <label>
              Profession:
              <input
                type="text"
                value={profession}
                onChange={(event) => setProfession(event.target.value)}
              />
            </label>
            <br />
            <label>
              University:
              <input
                type="text"
                value={university}
                onChange={(event) => setUniversity(event.target.value)}
              />
            </label>
          </>
        )}
        {role === 'SPEAKER' && (
          <>
            <br />
            <label>
              Expertise:
              <input
                type="text"
                value={expertise}
                onChange={(event) => setExpertise(event.target.value)}
              />
            </label>
          </>
        )}
        {role === 'ORGANIZER' && (
          <>
            <br />
            <label>
              Organization:
              <input
                type="text"
                value={organization}
                onChange={(event) => setOrganization(event.target.value)}
              />
            </label>
          </>
        )}
        <br />
        <button type="submit">Sign Up</button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
    </div>
  );
};

export default SignUp;