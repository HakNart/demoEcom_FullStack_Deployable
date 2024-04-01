import React from 'react'
import { Routes, Route } from 'react-router-dom'
import { CartPage, DashboardPage, HomeIndex, Login, OrderPage, PageNotFound, ProductDetail, ProductsIndex, Register } from '../pages'
import { AuthProvider } from '../context/AuthContext'
// import { ProtectedRoute } from './ProtectedRoute'

// export default function AllRoutes() {
//   return (
//     <div>
//         <Routes>
//           <Route path='/' element={<HomeIndex/>}/>
//           <Route path='demoEcommerceReactApp/' element={<HomeIndex/>}/>
//           <Route path='products' element={<ProductsIndex/>} />
//           <Route path='products/:id' element={<ProductDetail/>}/>
//           <Route path='login' element={<Login/>}/>
//           <Route path='register' element={<Register/>}/>

//           <Route path='cart' element={<ProtectedRoute><CartPage/></ProtectedRoute>}/>
//           <Route path="order-summary" element={<ProtectedRoute><OrderPage/></ProtectedRoute>}/>
//           <Route path="dashboard" element={<ProtectedRoute><DashboardPage/></ProtectedRoute>}/>

//           <Route path="*" element={<PageNotFound/>} />
//         </Routes>
//     </div>
//   )
// }
