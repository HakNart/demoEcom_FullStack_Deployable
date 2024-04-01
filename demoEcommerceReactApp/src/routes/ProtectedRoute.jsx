import React from "react";
import { Navigate, Route } from "react-router-dom";
// import { useAuth } from "../context/AuthContext"
import { useAuth } from "../hooks/useAuth";
// import { getAccessToken, logout, refreshAuth, isTokenExpired} from "../services/authServices";
import { toast } from "react-toastify"; 
import { useEffect } from "react";
import { AuthService } from "../services/authServices";
import { useRefreshToken } from "../hooks/useRefreshToken";

export const ProtectedRoute= ({children})  => {
  const {isAuthenticated } = useAuth();
  const {refreshToken} = useRefreshToken();
  useEffect(() => {
    const checkAndRefreshToken = async() => {
      console.log("Protect route mounted")
      const isValidToken = AuthService.checkValidToken();
      console.log("Token valid: ", isValidToken)
      if (!isValidToken && isAuthenticated) {
        const response = await refreshToken();
      }
    }
    checkAndRefreshToken();
    const refreshInterval = setInterval(checkAndRefreshToken, 10*1000) // check every 1 minute
    return () => clearInterval(refreshInterval); // Clean up interval on  unmount
  }, [])
  // useEffect(() => 

  //   checkAndRefreshToken();
  //   return () => checkAndRefreshToken(); // Refresh token on each component mount
  // }, [isAuthenticated]);
  // const checkAndRefreshToken = async () => {
  //   if (!isAuthenticated) return // Skip if not authenticated

  //   let accessToken = getAccessToken();

  //   if (!accessToken) return // Skip if access token is missing

  //   if (!isTokenExpired(accessToken)) return; // Skip if access is not expired

  //   try {
  //     await refreshAuth();
  //     setIsAuthenticated(true); // Update authentication status after token refresh
  //   } catch (error) {
  //     toast.error(error.message);
  //     logout();
  //     setIsAuthenticated(false); // Update authentication status
  //   }
  // }
  // Handle jwt token expiry
  if (!isAuthenticated) {
    // Check if authentication has expired
    return <Navigate to="/login" />
  } else {
    // const isValidToken = AuthService.checkValidToken();
    // if (!isValidToken) {
    //   refreshToken();
    // }
    return children;
  }
}