import { useNavigate } from "react-router-dom";
import { useAuth } from "./useAuth"
import { AuthService } from "../services/authServices";
import { toast } from "react-toastify";
import { AuthConstants } from "../common/AuthConstants";
import { authReducerActions } from "../reducers/authReducer";
import { useLogout } from "./useLogout";

export const useRefreshToken = () => {
  const {dispatch} = useAuth();
  const navigate = useNavigate();
  const {logout} = useLogout();

  const refreshToken = async () => {
    const response = await AuthService.refreshAuth();
    const jsonRespone = await response.json();
    if (!response.ok) {
      // Handle when not successful, 
      toast.error(jsonRespone.message);
      // Logout when fail to refresh token
      logout();
      navigate("/login")
    } else {
      const data = jsonRespone.payload;
      sessionStorage.setItem(AuthConstants.TOKEN_KEY, JSON.stringify(data.accessToken));

    }
  }
  return {refreshToken};
}