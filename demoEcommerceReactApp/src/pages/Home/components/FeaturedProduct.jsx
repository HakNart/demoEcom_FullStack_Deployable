import React, { useEffect, useState } from 'react'
import { toast } from 'react-toastify';
import { ProductCard } from '../../../components/Elements/ProductCard'
import { getFeaturedList } from '../../../services/productServices';

export const FeaturedProduct = () => {
  const [products, setProducts] = useState([]);
  
  useEffect(() => {
    async function fetchProducts() {
      try {
        const readFeaturedList = await getFeaturedList();
        setProducts(readFeaturedList);
      } catch (err) {
        toast.error(err.message);
      }
      
    }
    fetchProducts();
  },[])
  return (
    <section className='my-20'>
      <h1 className='text-2xl text-center font-semibold dark:text-slate-100 mb-5 underline underline-offset-8'>
        Featured Products
      </h1>
      <div className='flex flex-wrap justify-center lg:flex-row'>
        {/* Product Card */}
        { products.map((product) => (
          <ProductCard key={product.id} product={product}/>
        ))}

      </div>
    </section>
  )
}
