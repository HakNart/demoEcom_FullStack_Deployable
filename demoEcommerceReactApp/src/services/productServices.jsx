import { AuthService } from "./authServices";

const hostUrl = import.meta.env.VITE_APP_HOST;
const api_version = "/api/v1"
const host = AuthService.baseUrl;
export async function getProductList(searchTerm) {
  const response = await fetch(`${host}/products?name_like=${searchTerm?searchTerm:""}`);
  if(!response.ok){
    const responseObject = await response.json();
    const errorMessage = responseObject.message? responseObject.message: "Unknown error";
    throw { message: errorMessage, status: response.status }; 
}
  const responseObject = await response.json();
  const data = responseObject.payload;
  return data;
}

export async function getProduct(id) {
  const response = await fetch(`${host}/products/${id}`);
  if(!response.ok){
    const responseObject = await response.json();
    const errorMessage = responseObject.message? responseObject.message: "Unknown error";
    throw { message: errorMessage, status: response.status }; 
} 
  const responseObject = await response.json();
  const data = responseObject.payload;
  return data;
}

export async function getFeaturedList() {
  // const response = await fetch(`${host}/featured_products`);
  const response = await fetch(`${host}/products?featured=true`);
  if(!response.ok){
    const responseObject = await response.json();
    const errorMessage = responseObject.message? responseObject.message: "Unknown error";
    throw { message: errorMessage, status: response.status }; 
}
  const responseObject = await response.json();
  const data = responseObject.payload;
  return data;
}

/***
Json reponse format
{
    "status": "success",
    "payload": []
}
***/