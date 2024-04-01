import { useState } from 'react'

import './App.css'
import { Footer, Header } from './components'
// import AllRoutes from './routes/AllRoutes'
import { AuthProvider } from './context/AuthContext'
import { Routes, Route, BrowserRouter } from 'react-router-dom'
import {ProtectedRoute}   from './routes/ProtectedRoute'
// import { DashboardPage } from './pages'

import { CartPage, DashboardPage, HomeIndex, Login, OrderPage, PageNotFound, ProductDetail, ProductsIndex, Register } from './pages'
function App() {

  return (

    <div className="App">
      <Header/>
      {/* <AllRoutes/> */}
        <Routes>
          <Route path='/' element={<HomeIndex/>}/>
          <Route path='demoEcommerceReactApp/' element={<HomeIndex/>}/>
          <Route path='products' element={<ProductsIndex/>} />
          <Route path='products/:id' element={<ProductDetail/>}/>
          <Route path='login' element={<Login/>}/>
          <Route path='register' element={<Register/>}/>

          <Route path='cart' element={<ProtectedRoute><CartPage/></ProtectedRoute>}/>
          <Route path="order-summary" element={<ProtectedRoute><OrderPage/></ProtectedRoute>}/>
          <Route path="dashboard" element={<ProtectedRoute><DashboardPage/></ProtectedRoute>}/>

          <Route path="*" element={<PageNotFound/>} />
        </Routes>
      <Footer/>
    </div>
    
  )
}

export default App
