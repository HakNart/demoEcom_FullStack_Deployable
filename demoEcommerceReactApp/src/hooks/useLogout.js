import {AuthConstants} from "../common/AuthConstants";
import { authReducerActions } from "../reducers/authReducer";
import { AuthService } from "../services/authServices";
import { useAuth } from "./useAuth"

export const useLogout = () => {
  const {dispatch} = useAuth();

  // TODO: clean up carts and filter reducer as well

  const logout = async () => {
    // Remove user from storage
    await AuthService.logout();

    
    sessionStorage.removeItem(AuthConstants.TOKEN_KEY);
    sessionStorage.removeItem(AuthConstants.USERID_KEY);
    sessionStorage.removeItem(AuthConstants.REFRESH_TOKEN_KEY);
    // dispatch logout action
    dispatch({
      type: authReducerActions.LOGOUT,
      // No payload
    })

  }
  return {logout}
}