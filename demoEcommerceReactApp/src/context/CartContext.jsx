import { createContext, useContext, useReducer } from "react"
import { cartReducer, cartReducerActions } from "../reducers/cartReducer";

const cartInitalState = {
  cartList: [],
  total: 0,
}

const CartContext = createContext(cartInitalState);

export const CartProvider = ({children}) => {
  const [state, dispatch] = useReducer(cartReducer, cartInitalState);
  const dispatchAction = cartReducerActions;

  const addToCart = (product) => {
    // const updatedList = state.cartList.concat(product); // we don't want to change the cartList itself
    const updatedList = state.cartList.concat(product);
    const updateTotal = state.total + product.price;

    dispatch({
      type: dispatchAction.ADD_TO_CART,
      payload: {
        products: updatedList,
        total: updateTotal,
      }
    })
  }
  function removeFromCart(product) {
    const updatedList = state.cartList.filter(item => item.id !== product.id);
    const updatedTotal = state.total - product.price;

    dispatch({
      type: dispatchAction.REMOVE_FROM_CART,
      payload: {
        products: updatedList,
        total: updatedTotal,
      }
    })
  }
  function clearCart() {
    dispatch({
      type: dispatchAction.CLEAR_CART,
      payload: {
        products: [],
        total: 0,
      }
    })
  }

  const value = {
    cartList: state.cartList,
    total: state.total,
    addToCart,
    removeFromCart,
    clearCart,
  }

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  )
}

export const useCart = () => useContext(CartContext);