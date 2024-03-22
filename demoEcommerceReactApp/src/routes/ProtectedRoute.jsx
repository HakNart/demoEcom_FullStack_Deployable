import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext"
import { getAccessToken, logout, refreshAuth, isTokenExpired} from "../services/authServices";
import { toast } from "react-toastify"; 
import { useEffect } from "react";

export const ProtectedRoute = ({ children }) => {
  const {isAuthenticated, setIsAuthenticated } = useAuth();

  useEffect(() => {

    checkAndRefreshToken();
    return () => checkAndRefreshToken(); // Refresh token on each component mount
  }, [isAuthenticated]);
  const checkAndRefreshToken = async () => {
    if (!isAuthenticated) return // Skip if not authenticated

    let accessToken = getAccessToken();

    if (!accessToken) return // Skip if access token is missing

    if (!isTokenExpired(accessToken)) return; // Skip if access is not expired

    try {
      await refreshAuth();
      setIsAuthenticated(true); // Update authentication status after token refresh
    } catch (error) {
      toast.error(error.message);
      logout();
      setIsAuthenticated(false); // Update authentication status
    }
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" />
  }
  return children;
}