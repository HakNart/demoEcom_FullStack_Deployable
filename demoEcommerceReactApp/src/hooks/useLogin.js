import { useState } from "react"
import { useAuth } from "./useAuth";
import {AuthConstants} from "../common/AuthConstants";
import { authReducerActions } from "../reducers/authReducer";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { AuthService } from "../services/authServices";



export const useLogin = () => {
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(null);
  // Load dispatch function from context provided  by AuthProvider
  const { dispatch } = useAuth();
  const navigate = useNavigate();

  // Login function - provide to component that call for this hook
  const login = async (loginDetail) => {
    // Set loading to true first - Loading screen component can check this state to be rendered
    setIsLoading(true);
    setError(null);

    const response = await AuthService.login(loginDetail)
    const jsonReponse = await response.json();
    if (!response.ok) {
      setIsLoading(false);
      setError(jsonReponse.message);
      toast.error(error);
    } else {
      // Extract data from response payload
      const data = jsonReponse.payload;
      // Set auth data to session storage
      sessionStorage.setItem(AuthConstants.TOKEN_KEY, JSON.stringify(data.accessToken));
      sessionStorage.setItem(AuthConstants.REFRESH_TOKEN_KEY, JSON.stringify(data.refreshToken));
      sessionStorage.setItem(AuthConstants.USERID_KEY, JSON.stringify(data.id));     
      toast.success("Login user successfully");

      // update the context
      dispatch({
        type: authReducerActions.LOGIN,
      })
      
      // Update laoding state
      setIsLoading(false);
      // Navigate to the product listing page
      navigate("/products");

    }
    return jsonReponse;
  }

  return { login, isLoading, error}
}