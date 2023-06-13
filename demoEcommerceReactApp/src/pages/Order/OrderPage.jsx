import React from 'react'
import { useLocation } from 'react-router-dom';
import { useTitle } from '../../hooks/useTitle'
import { OrderFail } from './components/OrderFail';
import { OrderSuccess } from './components/OrderSuccess';

export  function OrderPage() {
  useTitle("Order Summary");
  const {state} = useLocation();
  return (
    <div>
      {state.status ? <OrderSuccess data={state.data} /> : <OrderFail/>}
    </div>
  )
}
