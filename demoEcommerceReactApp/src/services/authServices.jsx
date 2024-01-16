const host = import.meta.env.VITE_APP_HOST;
export async function login(authDetail) {
  const requestOptions = {
    method: "POST",
    headers: {"content-Type": "application/json"},
    body: JSON.stringify(authDetail)
  }

  const response = await fetch(`${host}/auth/login`, requestOptions);
  if(!response.ok){
      const responseObject = await response.json();
      const errorMessage = responseObject.message? responseObject.message: "Unknown error";
      throw { message: errorMessage, status: response.status }; 
  }
  const responseObject = await response.json();
  const data = responseObject.payload;
  if(data.accessToken){
      sessionStorage.setItem("token", JSON.stringify(data.accessToken));

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
    const errorMessage = responseObject.message? responseObject.message: "Unknown error";
    throw { message: errorMessage, status: response.status }; 
  }
  const responseObject = await response.json();
  const data = responseObject.payload;
  if(data.accessToken){
      sessionStorage.setItem("token", JSON.stringify(data.accessToken));
      // sessionStorage.setItem("uid", JSON.stringify(data.user.id));
      sessionStorage.setItem("uid", JSON.stringify(data.id));
  }

  return data;
}

export function logout(){
  sessionStorage.removeItem("token");
  sessionStorage.removeItem("uid");
}

function getSession() {
  const token = JSON.parse(sessionStorage.getItem("token"));
  const uid = JSON.parse(sessionStorage.getItem("uid"));
  return { token, uid };
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
    const errorMessage = responseObject.message? responseObject.message: "Unknown error";
    throw { message: errorMessage, status: response.status }; 
}
  const responseObject = await response.json();
  const data = responseObject.payload;
  return data;
}