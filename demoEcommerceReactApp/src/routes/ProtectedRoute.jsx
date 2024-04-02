import React from "react";
import { Navigate, Route } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { toast } from "react-toastify"; 
import { useEffect } from "react";
import { AuthService } from "../services/authServices";
import { useRefreshToken } from "../hooks/useRefreshToken";

export const ProtectedRoute= ({children})  => {
  const {isAuthenticated } = useAuth();
  const {refreshToken} = useRefreshToken();
  useEffect(() => {
    const checkAndRefreshToken = async() => {
      const isValidToken = AuthService.checkValidToken();
      if (!isValidToken && isAuthenticated) {
        const response = await refreshToken();
      }
    }
    checkAndRefreshToken();
    const refreshInterval = setInterval(checkAndRefreshToken, 10*1000) // check every 1 minute
    return () => clearInterval(refreshInterval); // Clean up interval on  unmount
  }, [])

  // Handle jwt token expiry
  if (!isAuthenticated) {
    // Check if authentication has expired
    return <Navigate to="/login" />
  } else {
    return children;
  }
}