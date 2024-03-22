import React, { createContext, useContext, useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import { checkAuthentication, getAccessToken, logout, refreshAuth, login } from '../services/authServices';
import { toast } from 'react-toastify';

const AuthContext = createContext();
export const AuthProvider =  ({children}) => {
  // let token = JSON.parse(sessionStorage.getItem("token"));

  // TODO: handle when token expires
  // if (token) {
  //   if (isTokenExpired(token)) {
  //     const refreshToken = JSON.parse(sessionStorage.getItem("refreshToken"));
  //     // TODO: HANDLE REFRESHTOKEN HERE

  //   }
  //   return children
  // const refreshToken = JSON.parse(sessionStorage.getItem("refreshToken"));
  // if (token && isTokenExpired(token)) {
  //   console.log("Check token expired!")
  //   async() => await refreshAuth(refreshToken)
  //   .then(res => {
//     token = JSON.parse(sessionStorage.getItem("token"));
  //     if (token) {
  //       return children;
  //     }
  //     console.log("Redirect to login .........")
  //     return <Navigate to="/login"/>
  //   })
  // }
  const [isAuthenticated, setIsAuthenticated] = useState(checkAuthentication());

  // useEffect(() => {
  //   const checkAndRefreshToken = async () => {
  //     console.log("Check if refresh token...")
  //     if (!isAuthenticated) return // Skip if not authenticated
  
  //     const accessToken = getAccessToken();
  //     if (!accessToken) return // Skip if access token is missing
  
  //     // const isTokenExpired = isTokenExpired(accessToken);
  //     if (!isTokenExpired(accessToken)) return; // Skip if access is not expired
  
  //     try {
  //       console.log("Refreshing token...")
  //       await refreshAuth();
  //       setIsAuthenticated(true); // Update authentication status after token refresh
  //     } catch (error) {
  //       toast.error(error.message);
  //       logout();
  //       setIsAuthenticated(false); // Update authentication status
  //     }
  //   } 

  //   checkAndRefreshToken();
  //   return () => checkAndRefreshToken(); // Refresh token on each component mount
  // }, [isAuthenticated]);
  const doLogin = async (authDetail) => {
    await login(authDetail);
    setIsAuthenticated(true);
  }
  const doLogout = () => {
    logout();
    setIsAuthenticated(false);
  }
  // checkAndRefreshToken();

  return (
    <AuthContext.Provider value={{isAuthenticated,setIsAuthenticated, doLogin, doLogout}}>
      {children}
    </AuthContext.Provider>
  )
 
}

export const useAuth = () => useContext(AuthContext);

function  isTokenExpired(token) {
  const bufferTimeInMinutes = 1;
  const decoded = jwtDecode(token);
  if (decoded.exp) {
    const currentTime = Date.now() / 1000;
    return currentTime > decoded.exp - bufferTimeInMinutes*60; 
  }
  return true;
}
