import { useState } from 'react'

import './App.css'
import { Footer, Header } from './components'
import AllRoutes from './routes/AllRoutes'
import { AuthProvider } from './context/AuthContext'

function App() {

  return (
    <div className="App">
    <AuthProvider>
      <Header/>
      <AllRoutes/>
      <Footer/>
    </AuthProvider>
    </div>
  )
}

export default App
