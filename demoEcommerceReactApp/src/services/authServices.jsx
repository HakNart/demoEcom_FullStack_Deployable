import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { toast } from "react-toastify";
const TOKEN_KEY = "token";
const REFRESH_TOKEN_KEY = "refreshToken";
const USERID_KEY = 'uid';

const hostUrl = import.meta.env.VITE_APP_HOST;
const api_version = "/api/v1"
const host = `${hostUrl}${api_version}`

export const AuthService = {
  baseUrl: host,
  login,
  register,
  logout,
  checkValidToken,
  getUser, 
  refreshAuth,
}

async function login(authDetail) {
  const requestOptions = {
    method: "POST",
    headers: {"content-Type": "application/json"},
    credentials: "include",
    body: JSON.stringify(authDetail)
    
  }

  return await fetch(`${host}/auth/login`, requestOptions);
}

async function register(authDetail){
  const requestOptions = {
      method: "POST",
      headers: {"content-Type": "application/json"},
      body: JSON.stringify(authDetail)
  }  
  return await fetch(`${host}/auth/register`, requestOptions);
}

async function refreshAuth() {
  // const refreshToken = getRefreshToken();
  // console.log("Refresh token", refreshToken
  const requestOptions = {
    method: "POST",
    // headers: {"content-type": "application/json"},
    credentials: 'include', // send the httponly refreshToken cokie to server
    // body: JSON.stringify({refreshToken: refreshToken}),
  }
  return await fetch(`${host}/auth/refreshToken`, requestOptions);
}

async function logout(){
  const browserData = getSession();


  const requestOptions = {
    method: "GET",
    credentials: 'include',
    headers: {
      Authorization: `Bearer ${browserData.token}`, 
    }
  }
  return await fetch(`${host}/auth/logout`, requestOptions);
}
function checkValidToken()  {
  const accessToken = getAccessToken();
  return accessToken && !isTokenExpired(accessToken); 
}
function getAccessToken() {
  return JSON.parse(sessionStorage.getItem(TOKEN_KEY));
}
function getSession() {
  const token = JSON.parse(sessionStorage.getItem("token"));
  const uid = JSON.parse(sessionStorage.getItem("uid"));
  return { token, uid };
}

async function getUser() {
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