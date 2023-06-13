import React from 'react'
import { Routes, Route } from 'react-router-dom'
import { CartPage, DashboardPage, HomeIndex, Login, OrderPage, PageNotFound, ProductDetail, ProductsIndex, Register } from '../pages'
import { AuthorizedRoute } from './AuthorizedRoute'

export default function AllRoutes() {
  return (
    <div>
      <Routes>
        <Route path='/' element={<HomeIndex/>}/>
        <Route path='demoEcommerceReactApp/' element={<HomeIndex/>}/>
        <Route path='products' element={<ProductsIndex/>} />
        <Route path='products/:id' element={<ProductDetail/>}/>
        <Route path='login' element={<Login/>}/>
        <Route path='register' element={<Register/>}/>
        {/* Todo: Implement protected route */}
        <Route path='cart' element={<AuthorizedRoute><CartPage/></AuthorizedRoute>}/>
        <Route path="order-summary" element={<AuthorizedRoute><OrderPage/></AuthorizedRoute>} />
        <Route path="dashboard" element={<AuthorizedRoute><DashboardPage/></AuthorizedRoute>} />

        <Route path="*" element={<PageNotFound/>} />
      </Routes>
    </div>
  )
}
