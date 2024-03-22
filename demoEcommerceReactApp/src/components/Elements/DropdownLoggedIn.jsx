import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useCart } from '../../context/CartContext';
import { logout } from '../../services/authServices';
import { useAuth } from '../../context/AuthContext';

export const DropdownLoggedIn = ({setDropdown}) => {
  const navigate = useNavigate();
  const [user, setUser] = useState({});
  const {clearCart} = useCart();
  const {isAuthenticated, doLogout} = useAuth();

  // useEffect(() =>{
  //   async function fetchData() {
  //     const response = await fetch("")
  //   }
  // }, [])
  const handleLogout = () => {
    doLogout();
    clearCart();
    setDropdown(false);
  }
  return (
    <div id="dropdownAvatar" className="select-none	absolute top-10 right-0 z-10 w-44 bg-white rounded divide-y divide-gray-100 shadow dark:bg-gray-700 dark:divide-gray-600">
        <div className="py-3 px-4 text-sm text-gray-900 dark:text-white">
            <div className="font-medium truncate">{user.email}</div>
        </div>
        <ul className="py-1 text-sm text-gray-700 dark:text-gray-200" aria-labelledby="dropdownUserAvatarButton">
            <li>
                <Link onClick={() => setDropdown(false)} to="/products" className="block py-2 px-4 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">All Products</Link>
            </li>
            <li>
                <Link onClick={() => setDropdown(false)} to="/dashboard" className="block py-2 px-4 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">Dashboard</Link>
            </li>
        </ul>
        <div className="py-1">
            <span onClick={handleLogout} className="cursor-pointer block py-2 px-4 text-sm text-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-200 dark:hover:text-white">Log out</span>
        </div>
    </div>
  )
}
