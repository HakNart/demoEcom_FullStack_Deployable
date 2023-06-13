import React from 'react'
import { useTitle } from '../../hooks/useTitle'
import { Faq } from './components/Faq'
import { FeaturedProduct } from './components/FeaturedProduct'
import { Hero } from './components/Hero'

export function HomeIndex() {
  useTitle("Greatest Kitchen Store")
  return (
    <main>
      <Hero/>
      <FeaturedProduct/>
      {/* Can have testimonial here. Not implement in this case */}
      {/* <Testimonials/> */}
      {/* FAQ */}
      <Faq/>
    </main>
  )
}






