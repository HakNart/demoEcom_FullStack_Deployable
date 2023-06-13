import { createContext, useContext, useReducer } from "react"
import { filterReducer } from "../reducers/FilterReducer";

const filterInitalState = {
  productList: [],
  onlyInStock: false,
  bestSellersOnly: false,
  sortBy: null,
  rating: null,
}

const FilterContext = createContext(filterInitalState);

export const FilterProvider = ({children}) => {
  const [state, dispatch] = useReducer(filterReducer, filterInitalState);

  const initalProductList = (products) => {
    dispatch({
      type: 'PRODUCT_LIST',
      payload: {
        products: products,
      }
    });
    
  }

  const bestSeller = (products) => {
    
    return state.bestSellerOnly ? products.filter(product => product.best_seller === true) : products;

  }
  const inStock = (products) => {
    return state.onlyInStock ? products.filter(product => product.in_stock === true): products;
  }
  const sort = (products) => {
    if (state.sortBy === 'low to high') {
      return products.sort((a,b) => Number(a.price) - Number(b.price));
    }
    else if (state.sortBy === 'high to low') {
      return products.sort((a,b) => Number(b.price) - Number(a.price));
    }
    return products;
  }
  const rating = (products) => {
    if (!state.rating) {
      return products;
    } 
    let starFilter = 0;
    switch(state.rating) {
      case "above4":
        starFilter = 4;
        break;
      case "above3":
        starFilter = 3;
        break;
      case "above2":
        starFilter = 2;
        break;
      case "above1":
        starFilter = 1;
        break;
    }
    return products.filter(product => product.rating >= starFilter);
  }
  
  const filterProductList = rating(sort(inStock(bestSeller(state.productList))));

  const value = {
    state,
    dispatch,
    products: filterProductList,
    initalProductList,
  };
  // console.log(value);
  return (
    <FilterContext.Provider value={value}>
      {children}
    </FilterContext.Provider>
  )
}

// Return the context object 'value'
export const useFilter = () => useContext(FilterContext);