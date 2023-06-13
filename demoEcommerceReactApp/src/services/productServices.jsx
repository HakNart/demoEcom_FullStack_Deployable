const host = import.meta.env.VITE_APP_HOST;
export async function getProductList(searchTerm) {
  const response = await fetch(`${host}/products?name_like=${searchTerm?searchTerm:""}`);
  if(!response.ok) {
    throw { message: response.statusText, status: response.status};
  }
  const data = await response.json();
  return data;
}

export async function getProduct(id) {
  const response = await fetch(`${host}/products/${id}`);
  if(!response.ok) {
    throw { message: response.statusText, status: response.status};
  }  
  const data = await response.json();
  return data;
}

export async function getFeaturedList() {
  // const response = await fetch(`${host}/featured_products`);
  const response = await fetch(`${host}/products?featured=true`);
  if(!response.ok) {
    throw { message: response.statusText, status: response.status};
  }  
  const data = await response.json();
  return data;
}