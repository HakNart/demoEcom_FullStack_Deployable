import { useContext } from "react"
import { AuthContext, AuthProvider } from "../context/AuthContext"

/**This step is used to validate if context is loaded properly
 * Other authentication hooks (e.g., useLogin, useLogout) will load context through this hook primarily
 */
export const useAuth = () => {
  const context = useContext(AuthContext);

  if (!context) {
    throw Error('useAuthContext must be used inside AuthProvider');
  }

  return context;
}