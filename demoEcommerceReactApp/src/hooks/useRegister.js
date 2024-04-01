import { useState } from "react"
import { useAuth } from "./useAuth";
import {AuthConstants} from "../common/AuthConstants";
import { authReducerActions } from "../reducers/authReducer";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { AuthService } from "../services/authServices";

export const useRegister = () => {
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(null);
  // Load dispatch function from context provided  by AuthProvider
  const { dispatch } = useAuth();
  const navigate = useNavigate();

  // Login function - provide to component that call for this hook
  const register = async (registerDetail) => {
    // Set loading to true first - Loading screen component can check this state to be rendered
    setIsLoading(true);
    setError(null);
  
    const response = await AuthService.register(registerDetail);
    const jsonReponse = await response.json();

    if (!response.ok) {
      setIsLoading(false);
      setError(jsonReponse.error);
      toast.error(jsonReponse.message);
    } else if (jsonReponse.payload.accessToken) {
      const data = jsonReponse.payload;
      sessionStorage.setItem(AuthConstants.TOKEN_KEY, JSON.stringify(data.accessToken));
      sessionStorage.setItem(AuthConstants.REFRESH_TOKEN_KEY, JSON.stringify(data.refreshToken));
      sessionStorage.setItem(AuthConstants.USERID_KEY, JSON.stringify(data.id));     
      toast.success("Register user successfully")

      // update the context
      dispatch({
        type: authReducerActions.LOGIN,
      })

      // Update laoding state
      setIsLoading(false);

      navigate("/products")
    }
  }

  return { register, isLoading, error}
}