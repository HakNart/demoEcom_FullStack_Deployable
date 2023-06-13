import React from 'react'
import { Link } from 'react-router-dom'

export const Hero = () => {
  return (
    <section className='flex flex-col lg:flex-row items-center'>
      <div className='text my-5'>
        <h1 className='text-5xl font-bold'>The Kitchen Store</h1>
        <p className='text-2xl my-7 px-1'>The KT store is the world's  most popular and authoritative source for kitchen equipments. Find ratings and access to the newest kitchen tools digitally</p>
        <Link to="/products" type="button" className='text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-base px-5 py-2.5 mr-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800'>Explore kitchen tools</Link>
      </div>
      <div className='visual my-5 lg:max-w-xl'>
        <img className='rounded-lg max-h-full' src="https://res.cloudinary.com/dawui1ip0/image/upload/v1679717213/Ecommerce_demo/hero_b616gl.jpg" alt='Hero image'/>
        
      </div>
    </section>
  )
}
