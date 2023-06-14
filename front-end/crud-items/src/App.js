import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import 'tailwindcss/tailwind.css';
import Items from './pages/Items';
import Cookies from 'js-cookie';
import Monsters from './pages/Monsters';
import axios from 'axios';

import { v4 as uuidv4 } from 'uuid';


const API_URL = 'http://localhost:8080/items'; // Reemplaza con la URL de tu API

function App() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loggedIn, setLoggedIn] = useState(Cookies.get('token') != null);
    const [jwt, setJwt] = useState(null);
    const [validJwt, setValidJwt] = useState(false)

    useEffect(() => {
        if (jwt) {
            Cookies.set('token', jwt, { expires: 14 });


        }
    }, [jwt]);

    function handleUsernameChange(event) {
        setUsername(event.target.value);
    }

    function handlePasswordChange(event) {
        setPassword(event.target.value);
    }



    async function handleSubmit(event) {
        event.preventDefault();

        const formData = {
            username: username,
            password: password
        };

        try {
            const response = await fetch('http://localhost:8080/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                const data = await response.json();
                setJwt(data.token);
                setLoggedIn(true);

            } else {
                window.alert("error")
                console.error('Error de inicio de sesión:', response.statusText);
            }
        } catch (error) {
            console.error('Error de inicio de sesión:', error);
        }
    }


    return (

        <div>
            {!loggedIn && (
                <div className="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50">
                    <div className="bg-white p-4 w-2/5 rounded">
                        <div className="bg-white rounded shadow-md p-4">
                            <form className="Auth-form" onSubmit={handleSubmit}>
                                <div className="Auth-form-content">
                                    <h3 className="Auth-form-title">Inicio de sesión</h3>

                                    <label
                                        className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Username</label>
                                    <input
                                        type="username"
                                        className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                                        placeholder="Enter username"
                                        value={username}
                                        onChange={handleUsernameChange}
                                    />
                                    <label
                                        className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
                                    <input
                                        type="password"
                                        className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                                        placeholder="Enter password"
                                        value={password}
                                        onChange={handlePasswordChange}
                                    />

                                    <div className="d-grid gap-2 mt-3">
                                        <button type="submit"
                                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                                            Log in
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}
            {loggedIn && (
                <BrowserRouter>
                    <nav className="flex justify-center">
                        <ul className="flex space-x-4">
                            <li>
                                <Link
                                    to="/items"
                                    className="text-blue-500 hover:text-blue-700 font-medium"
                                >
                                    Items
                                </Link>
                            </li>
                            <li>
                                <Link
                                    to="/monsters"
                                    className="text-blue-500 hover:text-blue-700 font-medium"
                                >
                                    Monsters
                                </Link>
                            </li>
                        </ul>
                    </nav>
                    <Routes>
                        <Route path="/items" element={<Items />} />
                        <Route path="/monsters" element={<Monsters />} />
                    </Routes>
                </BrowserRouter>
            )}

        </div>

    );
}

export default App;
