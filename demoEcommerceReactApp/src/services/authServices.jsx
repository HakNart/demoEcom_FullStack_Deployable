import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { toast } from "react-toastify";
const TOKEN_KEY = "token";
const REFRESH_TOKEN_KEY = "refreshToken";
const USERID_KEY = 'uid';
const hostUrl = import.meta.env.VITE_APP_HOST;
const api_version = "/api/v1"
const host = `${hostUrl}${api_version}`

export async function login(authDetail) {
  const requestOptions = {
    method: "POST",
    headers: {"content-Type": "application/json"},
    body: JSON.stringify(authDetail)
  }

  const response = await fetch(`${host}/auth/login`, requestOptions);
  if(!response.ok){
      const responseObject = await response.json();
      const errorMessage = responseObject.message? responseObject.message: "Internal error";
      throw { message: errorMessage, status: response.status }; 
  }
  const responseObject = await response.json();
  const data = responseObject.payload;
  if(data.accessToken){
      sessionStorage.setItem("token", JSON.stringify(data.accessToken));
      sessionStorage.setItem("refreshToken", JSON.stringify(data.refreshToken))
      sessionStorage.setItem("uid", JSON.stringify(data.id));
  }
  return data;
}

export async function register(authDetail){
  const requestOptions = {
      method: "POST",
      headers: {"content-Type": "application/json"},
      body: JSON.stringify(authDetail)
  }  
  // const response = await fetch(`${host}/register`, requestOptions);
  const response = await fetch(`${host}/auth/register`, requestOptions);
  if(!response.ok){
    const responseObject = await response.json();
    const errorMessage = responseObject.message? responseObject.message: "Internal error";
    throw { message: errorMessage, status: response.status }; 
  }
  const responseObject = await response.json();
  const data = responseObject.payload;
  if(data.accessToken){
      sessionStorage.setItem("token", JSON.stringify(data.accessToken));
      sessionStorage.setItem("refreshToken", JSON.stringify(data.refreshToken))
      sessionStorage.setItem("uid", JSON.stringify(data.id));
  }

  return data;
}

export async function refreshAuth() {
  const refreshToken = getRefreshToken();
  console.log("Refresh token:" + refreshToken)
  const requestOptions = {
    method: "POST",
    headers: {"content-type": "application/json"},
    body: JSON.stringify({refreshToken: refreshToken}),
  }
  const response = await fetch(`${host}/auth/refreshToken`, requestOptions);
  if (!response.ok) {
    const responseObject = await response.json();
    const errorMessage = responseObject.message? responseObject.message: "Internal error";
    toast.error(errorMessage);
    throw { message: errorMessage, status: response.status }; 
  }
  const responseObject = await response.json();
  console.log(responseObject);
  const data = responseObject.payload;
  if (data.accessToken) {
    sessionStorage.setItem("token", JSON.stringify(data.accessToken));
  }
  return data.accessToken;
}

export function logout(){
  sessionStorage.removeItem(TOKEN_KEY);
  sessionStorage.removeItem(USERID_KEY);
  sessionStorage.removeItem(REFRESH_TOKEN_KEY);
}
export function checkAuthentication()  {
  const accessToken = getAccessToken();
  return accessToken && isTokenExpired(accessToken); 
}
export function getAccessToken() {
  return JSON.parse(sessionStorage.getItem(TOKEN_KEY));
}
export function getRefreshToken() {
  return JSON.parse(sessionStorage.getItem(REFRESH_TOKEN_KEY));
}
function getSession() {
  const token = JSON.parse(sessionStorage.getItem("token"));
  const uid = JSON.parse(sessionStorage.getItem("uid"));
  const refreshToken = JSON.parse(sessionStorage.getItem("refreshToken"));
  return { token, uid, refreshToken };
}

export const getUser = async () => {
  const browserData = getSession();
  const requestOptions = {
    method: "GET",
    headers: { 
      "Content-Type": "application/json", 
      Authorization: `Bearer ${browserData.token}` 
    }
  }
  // const response = await fetch(`${host}/660/users/${browserData.uid}`, requestOptions);
  // const response = await fetch(`${host}/users/${browserData.uid}`, requestOptions);
  const response = await fetch(`${host}/users/self`, requestOptions);
  if(!response.ok){
    const responseObject = await response.json();
    const errorMessage = responseObject.message? responseObject.message: "Internal error";
    throw { message: errorMessage, status: response.status }; 
}
  const responseObject = await response.json();
  console.log(responseObject);
  const data = responseObject.payload;
  return data;
}

export function   isTokenExpired(token) {
  const bufferTimeInMinutes = 1;
  const decoded = jwtDecode(token);
  if (decoded.exp) {
    const currentTime = Date.now() / 1000;
    return currentTime > decoded.exp - bufferTimeInMinutes*60; 
  }
  return true;
}