export const cartReducer = (state, action) => {
  const {type, payload} = action;

  switch(type) {
    case 'ADD_TO_CART':
      return {
        ...state,
        cartList: payload.products,
        total: payload.total,
      };
    case 'REMOVE_FROM_CART':
      return {
        ...state,
        cartList: payload.products,
        total: payload.total,
      };
    case 'CLEAR_CART':
      return {
        ...state,
        cartList: payload.products,
        total: payload.total,
      };
    default:
      throw new Error("No valid case")
  }
}
export const cartReducerActions = {
  ADD_TO_CART: 'ADD_TO_CART',
  REMOVE_FROM_CART: 'REMOVE_FROM_CART',
  CLEAR_CART: 'CLEAR_CART'
}