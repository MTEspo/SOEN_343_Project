import React, { useState, useEffect } from 'react';
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
  const [tags, setTags] = useState([]);
  const [selectedTags, setSelectedTags] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTags = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/tags/all-tags');
        setTags(response.data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchTags();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = {
      username,
      email,
      password,
      role: role.toUpperCase(),
      interests: selectedTags,
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
      console.log(response);
      alert("Signed up successfully! You may now verify your email");
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

  const handleTagSelect = (tag) => {
    if (selectedTags.includes(tag)) {
      setSelectedTags(selectedTags.filter((t) => t !== tag));
    } else if (selectedTags.length < 3) {
      setSelectedTags([...selectedTags, tag]);
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-[#E3D5C8]">
      <div className="bg-white shadow-lg rounded-lg p-8 w-96">
        <h2 className="text-2xl font-bold text-[#2E2E2E] text-center mb-4">Sign Up</h2>
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-[#5A5958] font-medium">Username:</label>
            <input
              type="text"
              value={username}
              onChange={(event) => setUsername(event.target.value)}
              placeholder="Enter username"
              className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
            />
          </div>
          <div>
            <label className="block text-[#5A5958] font-medium">Email:</label>
            <input
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              placeholder="Enter email"
              className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
            />
          </div>
          <div>
            <label className="block text-[#5A5958] font-medium">Password:</label>
            <input
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="Enter password"
              className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
            />
          </div>
          <div>
            <label className="block text-[#5A5958] font-medium">Role:</label>
            <select
              value={role}
              onChange={(event) => setRole(event.target.value)}
              className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
            >
              <option value="">Select a role</option>
              {roles.map((role) => (
                <option key={role.value} value={role.value}>
                  {role.label}
                </option>
              ))}
            </select>
          </div>
          {role === 'ATTENDEE' && (
          <>
            <div>
              <label className="block text-[#5A5958] font-medium">Profession:</label>
              <input
                type="text"
                value={profession}
                onChange={(event) => setProfession(event.target.value)}
                placeholder="Enter profession"
                className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
              />
            </div>
            <div>
              <label className="block text-[#5A5958] font-medium">University:</label>
              <input
                type="text"
                value={university}
                onChange={(event) => setUniversity(event.target.value)}
                placeholder="Enter university"
                className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
              />
            </div>
            <div>
              <label className="block text-[#5A5958] font-medium">Interests:</label>
              <div className="flex flex-wrap gap-2">
                {tags.map((tag) => (
                  <button
                    key={tag}
                    type="button"
                    className={`px-2 py-1 border border-[#C4A88E] rounded-md ${
                      selectedTags.includes(tag) ? 'bg-[#C4A88E] text-white' : 'bg-white text-[#2E2E2E]'
                    }`}
                    onClick={() => handleTagSelect(tag)}
                  >
                    {tag}
                  </button>
                ))}
              </div>
              <p className="text-sm text-[#5A5958]">Select up to 3 interests</p>
            </div>
          </>
        )}
          {role === 'SPEAKER' && (
            <div>
              <label className="block text-[#5A5958] font-medium">Expertise:</label>
              <input
                type="text"
                value={expertise}
                onChange={(event) => setExpertise(event.target.value)}
                placeholder="Enter expertise"
                className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
              />
            </div>
          )}
          {role === 'ORGANIZER' && (
            <div>
              <label className="block text-[#5A5958] font-medium">Organization:</label>
              <input
                type="text"
                value={organization}
                onChange={(event) => setOrganization(event.target.value)}
                placeholder="Enter organization"
                className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
              />
            </div>
          )}
          <button
            type="submit"
            className="w-full bg-[#D9C2A3] text-[#2E2E2E] py-2 rounded-md font-semibold transition duration-300 hover:bg-[#C4A88E]"
          >
            Sign Up
          </button>
          {error && <p className="text-red-500 text-center mt-4">{error}</p>}
        </form>
      </div>
    </div>
  );
};

export default SignUp;