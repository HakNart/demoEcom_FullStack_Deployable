export const authReducer = (state, action) => {
  const {type, payload} = action;

  switch(type) {
    case 'LOGIN':
      return {
        ...state,
        isAuthenticated: true,      
      }
    case 'LOGOUT':
      return {
        ...state,
        isAuthenticated: false,
      }
    default:
      return state
  }
}

export const authReducerActions = {
  'LOGIN': 'LOGIN',
  'LOGOUT': 'LOGOUT',
}