import React, { useState, useEffect } from 'react';
import 'tailwindcss/tailwind.css';

import { v4 as uuidv4 } from 'uuid';

const API_URL = 'http://localhost:8080/monsters'; // Reemplaza con la URL de tu API

function Monsters() {
  const [loading, setLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);


  const [monsters, setMonsters] = useState([]);
  const [newMonster, setNewMonster] = useState({
    uuid: '',
    name: '',
    stats: {
      attributes: {
        strength: 0,
        dexterity: 0,
        constitution: 0,
        intelligence: 0,
        wisdom: 0,
        charisma: 0
      },
      armorClass: 0,
      initiative: 0,
      speed: 0,
      hitPoints: 0,
      hitDices: 0,
      deathSaves: 0
    }
  });
  const [selectedMonster, setSelectedMonster] = useState(null);
  const [showForm, setShowForm] = useState(false); // Estado para mostrar/ocultar el formulario de modificación
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [createForm, setCreateForm] = useState(false);


  useEffect(() => {
    fetchMonsters();
  }, []);

  const fetchMonsters = async () => {
    console.log(newMonster)
    try {
      setLoading(true);
      const response = await fetch(API_URL);
      const data = await response.json();
      setMonsters(data);
      setLoading(false);
    } catch (error) {
      setLoading(false);
      console.error('Error fetching monsters:', error);
    }
  };

  const createMonster = async () => {
    setFormLoading(true)
    console.log(newMonster)
    try {
      newMonster.uuid = uuidv4();
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newMonster),
      });
      if (response.ok) {
        setNewMonster({
          uuid: '',
          name: '',
          stats: {
            attributes: {
              strength: 0,
              dexterity: 0,
              constitution: 0,
              intelligence: 0,
              wisdom: 0,
              charisma: 0
            },
            armorClass: 0,
            initiative: 0,
            speed: 0,
            hitPoints: 0,
            hitDices: 0,
            deathSaves: 0
          }
        });
        setFormLoading(false)
        setCreateForm(false)
        fetchMonsters();
      } else {
        console.error('Error creating monster:', response.statusText);
      }
    } catch (error) {
      console.error('Error creating monster:', error);
    }
  };

  const updateMonster = async () => {
    try {
      const response = await fetch(`${API_URL}/${selectedMonster.uuid}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(selectedMonster),
      });

      if (response.ok) {
        await fetchMonsters();
      } else {
        console.error('Error updating monster:', response.statusText);
      }
    } catch (error) {
      console.error('Error updating monster:', error);
    }
  };

  const deleteMonster = async (uuid) => {
    try {
      const response = await fetch(`${API_URL}/${uuid}`, {
        method: 'DELETE',
      });
      if (response.ok) {
        await fetchMonsters();
      } else {
        console.error('Error deleting monster:', response.statusText);
      }
    } catch (error) {
      console.error('Error deleting monster:', error);
    }
  };

  const selectMonster = (monster) => {
    setSelectedMonster({ ...monster });
    setShowForm(true);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    setSelectedMonster((prevMonster) => ({
      ...prevMonster,
      [name]: value,
    }));
  };

  const handleClosePopup = () => {
    setShowForm(false);
    setCreateForm(false);
  };

  const selectMonsterUpdate = (item) => {
    setSelectedMonster({ ...item });
    setShowForm(true);
  }

  const handleCreateEvent = () => {
    setCreateForm(true);
  };

  const handleDeleteEvent = (monster) => {
    setSelectedMonster({ ...monster });
    setConfirmDelete(true);
  };

  const confirmDeleteMonster = async () => {
    console.log("estoy aquí")
    setConfirmDelete(false);
    if (selectedMonster) {
      await deleteMonster(selectedMonster.uuid);
    }
  };





  return (
    <div>
      <div className={`flex h-screen w-full justify-center items-center ${!loading && 'hidden'}`}>
        <img src="/loading.svg" width="250" />
      </div>
      <div className={`relative ${loading && 'hidden'}`}>

        <div>
          <button className=" absolute top-0  right-0 bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded" onClick={handleCreateEvent}>Crear Monstruo</button>
        </div>
        <div>
          <table className="absolute left-0 ">
            <thead>
              <tr className="even:bg-gray-100 odd:bg-white">
                <th className="py-2 px-4 border-b">Nombre</th>

                <th className="py-2 px-4 border-b">Strength</th>
                <th className="py-2 px-4 border-b">Dexterity</th>
                <th className="py-2 px-4 border-b">Constitution</th>
                <th className="py-2 px-4 border-b">Intelligence</th>
                <th className="py-2 px-4 border-b">Wisdom</th>
                <th className="py-2 px-4 border-b">Charisma</th>
                <th className="py-2 px-4 border-b">Armor Class</th>
                <th className="py-2 px-4 border-b">Initiative</th>
                <th className="py-2 px-4 border-b">Speed</th>
                <th className="py-2 px-4 border-b">HitPoints</th>
                <th className="py-2 px-4 border-b">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {monsters.map((monster, index) => (
                <tr className="even:bg-gray-100 odd:bg-white"
                  key={monster.name}
                  onClick={() => selectMonster(monster)}
                >
                  <td className="py-2 px-4 border-b">{monster.name}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.attributes.strength}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.attributes.dexterity}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.attributes.constitution}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.attributes.intelligence}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.attributes.wisdom}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.attributes.charisma}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.armorClass}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.initiative}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.speed}</td>
                  <td className="py-2 px-4 border-b">{monster.stats.hitPoints}</td>
                  <td className="py-2 px-4 border-b">
                    <div className="flex gap-2 justify-center">
                      <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={() => selectMonsterUpdate(monster)}>Modificar</button>
                      <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={() => handleDeleteEvent(monster)}>Eliminar</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {showForm && selectedMonster && (
          <div className="fixed inset-0 flex justify-center monsters-center bg-black bg-opacity-50">
            <div className="bg-white p-4 w-4/5 rounded">
              <div className="bg-white rounded shadow-md p-4">
                <h2>Editar Monstruo</h2>
                <label htmlFor="nombre" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Nombre</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="text"
                  name="nombre"
                  value={selectedMonster.name}
                  onChange={handleInputChange}
                />
                <label htmlFor="strength" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Strength</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="strength"
                  value={selectedMonster.attributes.stats.strength}
                  onChange={handleInputChange}
                />

<label htmlFor="nombre" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Nombre</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="text"
                  name="nombre"
                  value={newMonster.name}
                  onChange={(e) => setNewMonster({ ...newMonster, name: e.target.value })}
                  placeholder="Nombre"
                />


                <label htmlFor="strength" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Fuerza</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="strength"
                  value={selectMonster.stats.attributes.strength}

                  onChange={handleInputChange}
                  placeholder="Fuerza"
                />

                <label htmlFor="dexterity" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Destreza</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="dexterity"
                  value={newMonster.stats.attributes.dexterity}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, dexterity: parseInt(e.target.value) } } })}
                  placeholder="Destreza"
                />

                <label htmlFor="constitution" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Constitucion</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="constitution"
                  value={newMonster.stats.attributes.constitution}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, constitution: parseInt(e.target.value) } } })}
                  placeholder="Constitucion"
                />

                <label htmlFor="intelligence" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Inteligencia</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="intelligence"
                  value={newMonster.stats.attributes.intelligence}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, intelligence: parseInt(e.target.value) } } })}
                  placeholder="Inteligencia"
                />

                <label htmlFor="wisdom" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Sabiduria</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="wisdom"
                  value={newMonster.stats.attributes.wisdom}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, wisdom: parseInt(e.target.value) } } })}
                  placeholder="Sabiduria"
                />

                <label htmlFor="charisma" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Carisma</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="charisma"
                  value={newMonster.stats.attributes.charisma}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, charisma: parseInt(e.target.value) } } })}
                  placeholder="Carisma"
                />

                <label htmlFor="armorClass" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Armadura</label>

                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="armorClass"
                  value={newMonster.stats.armorClass}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, armorClass: parseInt(e.target.value) } })}

                  placeholder="Armadura"
                />

                <label htmlFor="speed" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Velocidad</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="speed"
                  value={newMonster.stats.speed}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, speed: parseInt(e.target.value) } })}
                  placeholder="Velocidad"
                />

                <label htmlFor="hitPoints" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Hit Points</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="hitPoints"
                  value={newMonster.stats.hitPoints}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, hitPoints: parseInt(e.target.value) } })}
                  placeholder="Hit Points"
                />
                <label htmlFor="hitDices" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Hit Dices</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="hitDices"
                  value={newMonster.stats.hitDices}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, hitDices: parseInt(e.target.value) } })}
                  placeholder="hitDices"
                />
                <label htmlFor="initiative" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Iniciativa</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="initiative"
                  value={newMonster.stats.initiative}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, initiative: parseInt(e.target.value) } })}
                  placeholder="Iniciativa"
                />
                <div className="flex gap-2 justify-center">
                  <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={updateMonster}>Actualizar</button>
                  <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={handleClosePopup}>Cancelar</button>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>

      {confirmDelete && selectedMonster && (
        <div>
          <div className="fixed inset-0 flex justify-center monsters-center bg-black bg-opacity-50">
            <div className="bg-white p-4 w-4/5 rounded">
              <div className="bg-white rounded shadow-md p-4">
                <p>¿Seguro que quieres eliminar este elemento?</p>
                <div className="flex gap-2 justify-center">
                  <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={confirmDeleteMonster}>Confirmar</button>
                  <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={() => setConfirmDelete(false)}>Cancelar</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {createForm && (
        <div>
          <div className="fixed inset-0 flex justify-center monsters-center bg-black bg-opacity-50">
            <div className="bg-white p-4 w-4/5 rounded overflow-auto">
              <div className="bg-white rounded shadow-md p-4">
                <h2>Crear Monstruo</h2>
                <label htmlFor="nombre" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Nombre</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="text"
                  name="nombre"
                  value={newMonster.name}
                  onChange={(e) => setNewMonster({ ...newMonster, name: e.target.value })}
                  placeholder="Nombre"
                />


                <label htmlFor="strength" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Fuerza</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="strength"
                  value={newMonster.stats.attributes.strength}

                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, strength: parseInt(e.target.value) } } })}
                  placeholder="Fuerza"
                />

                <label htmlFor="dexterity" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Destreza</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="dexterity"
                  value={newMonster.stats.attributes.dexterity}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, dexterity: parseInt(e.target.value) } } })}
                  placeholder="Destreza"
                />

                <label htmlFor="constitution" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Constitucion</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="constitution"
                  value={newMonster.stats.attributes.constitution}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, constitution: parseInt(e.target.value) } } })}
                  placeholder="Constitucion"
                />

                <label htmlFor="intelligence" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Inteligencia</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="intelligence"
                  value={newMonster.stats.attributes.intelligence}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, intelligence: parseInt(e.target.value) } } })}
                  placeholder="Inteligencia"
                />

                <label htmlFor="wisdom" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Sabiduria</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="wisdom"
                  value={newMonster.stats.attributes.wisdom}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, wisdom: parseInt(e.target.value) } } })}
                  placeholder="Sabiduria"
                />

                <label htmlFor="charisma" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Carisma</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="charisma"
                  value={newMonster.stats.attributes.charisma}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, attributes: { ...newMonster.stats.attributes, charisma: parseInt(e.target.value) } } })}
                  placeholder="Carisma"
                />

                <label htmlFor="armorClass" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Armadura</label>

                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="armorClass"
                  value={newMonster.stats.armorClass}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, armorClass: parseInt(e.target.value) } })}

                  placeholder="Armadura"
                />

                <label htmlFor="speed" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Velocidad</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="speed"
                  value={newMonster.stats.speed}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, speed: parseInt(e.target.value) } })}
                  placeholder="Velocidad"
                />

                <label htmlFor="hitPoints" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Hit Points</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="hitPoints"
                  value={newMonster.stats.hitPoints}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, hitPoints: parseInt(e.target.value) } })}
                  placeholder="Hit Points"
                />
                <label htmlFor="hitDices" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Hit Dices</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="hitDices"
                  value={newMonster.stats.hitDices}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, hitDices: parseInt(e.target.value) } })}
                  placeholder="hitDices"
                />
                <label htmlFor="initiative" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Iniciativa</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="initiative"
                  value={newMonster.stats.initiative}
                  onChange={(e) => setNewMonster({ ...newMonster, stats: { ...newMonster.stats, initiative: parseInt(e.target.value) } })}
                  placeholder="Iniciativa"
                />


                <button onClick={createMonster}
                  className={`bg-${formLoading ? 'red-300' : 'green-500'} ${formLoading ? 'cursor-not-allowed' : 'cursor-pointer'} hover:bg-${formLoading ? 'gray-300' : 'green-700'} text-white font-bold py-2 px-4 rounded`}
                >Crear</button>
                <button onClick={handleClosePopup} className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">Cancelar</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Monsters;
