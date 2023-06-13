import React, { useRef } from 'react'
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useTitle } from '../hooks/useTitle';
import { login } from '../services/authServices';

export function Login() {
  useTitle("Login");
  const navigate = useNavigate();

  const email = useRef();
  const password = useRef();

  const handleLogin = async (event) => {
    event.preventDefault();
    const authDetail = {
      email: email.current.value,
      password: password.current.value,
    }

    try {
      const getUserLogin = await login(authDetail);
      getUserLogin.accessToken ? navigate("/products") : toast.error(data);
    } catch (err) {
      toast.error(err.message);
    }
  }
  return (
    <main>
      <section>
        <p className="text-2xl text-center font-semibold dark:text-slate-100 my-10 underline underline-offset-8">Login</p>
      </section>        
        <form onSubmit={handleLogin}>
          <div className="mb-6">
              <label htmlFor="email" className="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Your Email</label>
              <input type="email"  ref={email} id="email" className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Email" required autoComplete="off" />
          </div>
          <div className="mb-6">
              <label htmlFor="password" className="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Password</label>
              <input type="password" ref={password} id="password" placeholder="Enter your password" className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required />
          </div>
          <button type="submit" className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Log In</button>
        </form>

    </main>
  )
}
