import React from 'react'
import { useCart } from '../../context/CartContext';
import { useTitle } from '../../hooks/useTitle';
import { CartEmpty } from './components/CartEmpty';
import { CartList } from './components/CartList';

export function CartPage() {
  const {cartList} = useCart();
  useTitle(`Cart (${cartList.length})`);

  return (
    <main>       
      { cartList.length ? <CartList /> : <CartEmpty /> }   
    </main>
  )
}
