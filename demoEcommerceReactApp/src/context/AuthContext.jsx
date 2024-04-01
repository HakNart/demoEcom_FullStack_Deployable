import React, { createContext, useContext, useEffect, useReducer, useState } from 'react'
import { Navigate } from 'react-router-dom';
// import { jwtDecode } from 'jwt-decode';
// import { checkAuthentication, getAccessToken, logout, refreshAuth, login } from '../services/authServices';
// import { toast } from 'react-toastify';
import { authReducer, authReducerActions } from '../reducers/authReducer';
import  { AuthConstants } from '../common/AuthConstants';


export const AuthContext = createContext();
export const AuthProvider =  ({children}) => {
  /**Set initial state for this context
   * isAuthenticated: false -> user is not authenticated
   */
  const [state, dispatch] = useReducer(authReducer, {
    isAuthenticated: false,
  })

  useEffect(() => {
    const uid = JSON.parse(localStorage.getItem(AuthConstants.USERID_KEY));
    AuthConstants
    if (uid) {
      dispatch({
        type: authReducerActions.LOGIN,
        // No payload
      })
    }
  }, [])


  // const doLogin = async (authDetail) => {
  //   await login(authDetail); //   setIsAuthenticated(true);
  // }
  // const doLogout = () => {
  //   logout();
  //   setIsAuthenticated(false);
  // }
  // checkAndRefreshToken();

  // console.log('AuthContext state: ', state);
  return (
    <AuthContext.Provider value={{...state, dispatch}}>
      {children}
    </AuthContext.Provider>
  )
 
}


// function  isTokenExpired(token) {
//   const bufferTimeInMinutes = 1;
//   const decoded = jwtDecode(token);
//   if (decoded.exp) {
//     const currentTime = Date.now() / 1000;
//     return currentTime > decoded.exp - bufferTimeInMinutes*60; 
//   }
//   return true;
// }
