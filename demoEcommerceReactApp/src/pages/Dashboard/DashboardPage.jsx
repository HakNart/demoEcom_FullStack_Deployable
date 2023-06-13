import React, { useEffect, useState } from 'react'
import { toast } from 'react-toastify';
import { useTitle } from '../../hooks/useTitle';
import { getUserOrders } from '../../services/orderServices';
import { DashboardEmpty } from './components/DashbardEmpty';
import { DashboardCard } from './components/DashboardCard';

export function DashboardPage() {
  const [orders, setOrders] = useState([]);
  useTitle("Dashboard");

  useEffect(()=> {
    // Fetch all orders
    async function fetchOrders() {
      try {
        const readOrders = await getUserOrders();
        setOrders(readOrders);
      } catch(err) {
        toast.error(err.message);
      }
    }
    fetchOrders();
  }, [])
  return (
    <main>
      <section>
        <p className="text-2xl text-center font-semibold dark:text-slate-100 my-10 underline underline-offset-8">My Dashboard</p>
      </section>

      <section>
        { orders.length && orders.map((order) => (
          <DashboardCard key={order.id} order={order} />
        )) }
      </section>

      <section>
        { !orders.length && <DashboardEmpty /> }
      </section>

    </main>
  )
}
