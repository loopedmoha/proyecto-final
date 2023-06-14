import React, { useState, useEffect } from 'react';
import 'tailwindcss/tailwind.css';
import axios from 'axios';
import { v4 as uuidv4 } from 'uuid';
import Cookies from 'js-cookie';


const API_URL = 'http://localhost:8080/items'; // Reemplaza con la URL de tu API

function Items() {
  const [loading, setLoading] = useState(false);

  const [items, setItems] = useState([]);
  const [newItem, setNewItem] = useState({
    uuid: '',
    nombre: '',
    precio: { oro: 0, plata: 0, cobre: 0 },
    descripcion: '',
    categoria: 'ARMA',
    magical: false,
    magicalTrait: null,
    vanilla: true,
    creator: null
  });
  const [selectedItem, setSelectedItem] = useState(null);
  const [showForm, setShowForm] = useState(false); // Estado para mostrar/ocultar el formulario de modificación
  const [createForm, setCreateForm] = useState(false);
  useEffect(() => {
    fetchItems();
  }, []);

  const [confirmDelete, setConfirmDelete] = useState(false);
  const [jwt, setJwt] = useState(null);

  useEffect(() => {
    if (jwt) {
      Cookies.set('token', jwt, { expires: 14 });
    }
  }, [jwt]);

  const fetchItems = async () => {
    try {
      setLoading(true);
      const response = await axios.get(API_URL);
      const data = response.data;
      setItems(data);
      setLoading(false);
    } catch (error) {
      setLoading(false);
      console.error('Error fetching items:', error);
    }
  };

  const createItem = async () => {
    try {
      const jwt = Cookies.get('token');
      window.alert(jwt);

      newItem.uuid = uuidv4();

      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwt}` // Agrega el encabezado de autorización
        },
        body: JSON.stringify(newItem)
      });

      if (response.ok) {
        setNewItem({
          uuid: '',
          nombre: '',
          precio: { oro: 0, plata: 0, cobre: 0 },
          descripcion: '',
          categoria: 'ARMA',
          magical: false,
          magicalTrait: null,
          vanilla: true,
          creator: null
        });
        fetchItems();
      } else {
        console.error('Error creating item:', response.statusText);
      }
    } catch (error) {
      console.error('Error creating item:', error);
    }
  };



  const updateItem = async () => {
    try {
      const jwt = Cookies.get('token')
      const response = await fetch(`${API_URL}/${selectedItem.uuid}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authentication': `Bearer ${jwt}`
        },
        body: JSON.stringify(selectedItem)
      });

      if (response.ok) {
        fetchItems();
      } else {
        console.error('Error updating item:', response.statusText);
      }
    } catch (error) {
      console.error('Error updating item:', error);
    }
  };

  const deleteItem = async (uuid) => {
    try {
      const response = await fetch(`${API_URL}/${uuid}`, {
        method: 'DELETE'
      });
      if (response.ok) {
        fetchItems();
      } else {
        console.error('Error deleting item:', response.statusText);
      }
    } catch (error) {
      console.error('Error deleting item:', error);
    }
  };

  const selectItem = (item) => {
    setSelectedItem({ ...item });
    //setShowForm(true); // Mostrar el formulario al seleccionar un item
  };

  const selectItemUpdate = (item) => {
    setSelectedItem({ ...item });
    setShowForm(true);
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    if (name === 'precio.oro' || name === 'precio.plata' || name === 'precio.cobre') {
      const [field, subfield] = name.split('.');
      setSelectedItem((prevItem) => ({
        ...prevItem,
        precio: {
          ...prevItem.precio,
          [subfield]: parseInt(value),
        },
      }));
    } else {
      setSelectedItem((prevItem) => ({
        ...prevItem,
        [name]: value,
      }));
    }
  };

  const handleClosePopup = () => {
    setShowForm(false);
    setCreateForm(false);
  };

  const handlePriceChange = (e) => {
    const price = { ...selectedItem.precio, [e.target.uuid]: parseInt(e.target.value) };
    setSelectedItem({ ...selectedItem, precio: price });
  };

  const handleCreateEvent = () => {
    setCreateForm(true)
  }
  const handleDeleteEvent = (item) => {
    setSelectedItem({ ...item });
    setConfirmDelete(true);
  };

  const confirmDeleteItem = async () => {
    setConfirmDelete(false);
    if (selectedItem) {
      await deleteItem(selectedItem.uuid);
    }
  };
  const tableStyle = {
    borderRadius: '2px',  // Aplica el radio de 2 píxeles
    border: '1px solid black'
  };

  const cellStyle = {
    border: '1px solid black',
    //padding: '8px'
  };

  const formStyle = {
    marginLeft: '100px  ' // Espacio para separar el formulario de la tabla
  };

  return (

    <div>
      

      <div className={`flex h-screen w-full justify-center items-center ${!loading && 'hidden'}`}>
        <img src="/loading.svg" width="250" />
      </div>
      <div className={`relative ${loading && 'hidden'} gap-2`}>
        <button className=" absolute right-0 bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded" onClick={handleCreateEvent}>Crear Item</button>
        <table className="mx-auto left-0 ">
          <thead>
            <tr className="even:bg-gray-100 odd:bg-white">
              <th className="py-2 px-4 border-b">Nombre</th>
              <th className="py-2 px-4 border-b">Categoría</th>
              <th className="py-2 px-4 border-b">Descripción</th>
              <th className="py-2 px-4 border-b">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {items.map((item, index) => (
              <tr className="even:bg-gray-100 odd:bg-white"
                key={item.nombre}
                onClick={() => selectItem(item)}
              >
                <td className="py-2 px-4 border-b">{item.nombre}</td>
                <td className="py-2 px-4 border-b">{item.categoria}</td>
                <td className="py-2 px-4 border-b">{item.descripcion}</td>
                <td className="py-2 px-4 border-b">
                  <div className="flex gap-2 justify-center">
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={() => selectItemUpdate(item)}>Modificar</button>
                    <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={() => handleDeleteEvent(item)}>Eliminar</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {showForm && selectedItem && (
          <div className="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50">
            <div className="bg-white p-4 w-4/5 rounded">
              <div className="bg-white rounded shadow-md p-4">

                <h2>Editar Item</h2>
                <label for="nombre" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Nombre</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="text"
                  name="nombre"
                  value={selectedItem.nombre}
                  onChange={handleInputChange}
                />
                <label for="descripcion" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Descripcion</label>
                <textarea
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="text"
                  name="descripcion"
                  value={selectedItem.descripcion}
                  onChange={handleInputChange}
                />
                <label for="oro" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Monedas de oro</label>

                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="oro"
                  value={selectedItem.precio.oro}
                  onChange={handleInputChange}
                />
                <label for="plata" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Monedas de plata</label>

                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="plata"
                  value={selectedItem.precio.plata}
                  onChange={handleInputChange}
                />
                <label for="cobre" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Monedas de cobre</label>

                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="cobre"
                  value={selectedItem.precio.cobre}
                  onChange={handleInputChange}
                />
                <label for="descripcion" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Descripcion</label>

                <textarea
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  name="descripcion"
                  value={newItem.descripcion}
                  onChange={(e) => setNewItem({ ...newItem, descripcion: e.target.value })}
                  placeholder="Descripción"
                />
                <label for="categoria" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Categoria</label>
                <select
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm"
                  name="categoria"
                  value={selectedItem.categoria}
                  onChange={handleInputChange}
                >
                  <option value="ARMA">ARMA</option>
                  <option value="ARMADURA">ARMADURA</option>
                  <option value="OBJETO">OBJETO</option>
                  <option value="CONSUMIBLE">CONSUMIBLE</option>
                </select>
                <div className="flex gap-2 justify-center">
                  <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={updateItem}>Actualizar</button>
                  <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={handleClosePopup}>Cancelar</button>
                </div>

              </div>
            </div>
          </div>

        )}
      </div>

      {confirmDelete && selectedItem && (
        <div>
          <div className="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50">
            <div className="bg-white p-4 w-4/5 rounded">
              <div className="bg-white rounded shadow-md p-4">
                <p>¿Seguro que quieres eliminar este elemento?</p>
                <div className="flex gap-2 justify-center">
                  <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={confirmDeleteItem}>Confirmar</button>
                  <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={() => setConfirmDelete(false)}>Cancelar</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {createForm && (
        <div>
          <div className="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50">
            <div className="bg-white p-4 w-4/5 rounded">
              <div className="bg-white rounded shadow-md p-4">
                <label for="nombre" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Nombre</label>
                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="text"
                  name="nombre"
                  value={newItem.nombre}
                  onChange={(e) => setNewItem({ ...newItem, nombre: e.target.value })}
                  placeholder="Nombre"
                />
                <label for="oro" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Monedas de oro</label>

                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="oro"
                  value={newItem.precio.oro}
                  onChange={(e) => setNewItem({ ...newItem, precio: { ...newItem.precio, oro: e.target.value } })}
                  placeholder="Precio en Oro"
                />
                <label for="plata" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Monedas de plata</label>

                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="plata"
                  value={newItem.precio.plata}
                  onChange={(e) => setNewItem({ ...newItem, precio: { ...newItem.precio, plata: e.target.value } })}
                  placeholder="Precio en Plata"
                />
                <label for="cobre" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Monedas de cobre</label>

                <input
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  type="number"
                  name="cobre"
                  value={newItem.precio.cobre}
                  onChange={(e) => setNewItem({ ...newItem, precio: { ...newItem.precio, cobre: e.target.value } })}
                  placeholder="Precio en Cobre"
                />
                <label for="descripcion" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Descripcion</label>

                <textarea
                  className="bg-gray-50 border border-gray-300 text-sm rounded block w-full p-2.5"
                  name="descripcion"
                  value={newItem.descripcion}
                  onChange={(e) => setNewItem({ ...newItem, descripcion: e.target.value })}
                  placeholder="Descripción"
                />
                <label for="categoria" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Categoria</label>

                <select
                  name="categoria"
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm"

                  value={newItem.categoria}
                  onChange={(e) => setNewItem({ ...newItem, categoria: e.target.value })}
                >
                  <option value="ARMA">ARMA</option>
                  <option value="ARMADURA">ARMADURA</option>
                  <option value="OBJETO">OBJETO</option>
                  <option value="CONSUMIBLE">CONSUMIBLE</option>
                </select>
                <button onClick={createItem} className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">Crear</button>
                <button onClick={handleClosePopup} className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">Cancelar</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Items;
