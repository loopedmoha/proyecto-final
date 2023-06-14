import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Items from "./Items";
import Monsters from "./Monsters";


function Main() {
    return (
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
    );

}
export default Main;

