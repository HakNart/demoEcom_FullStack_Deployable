const host = import.meta.env.VITE_APP_HOST;

function getSession() {
  const token = JSON.parse(sessionStorage.getItem("token"));
  const uid = JSON.parse(sessionStorage.getItem("uid"));
  return { token, uid };
}

export async function getUserOrders(){
  const browserData = getSession();
  const requestOptions = {
      method: "GET",
      headers: {"Content-Type": "application/json", Authorization: `Bearer ${browserData.token}`}
  }
  // const response = await fetch(`${host}/users/660/orders?user.id=${browserData.uid}`, requestOptions);
  const response = await fetch(`${host}/users/self/orders`, requestOptions);
  if(!response.ok){
    const responseObject = await response.json();
    const errorMessage = responseObject.message? responseObject.message: "Unknown error";
    throw { message: errorMessage, status: response.status }; 
}
  const responseObject = await response.json();
  const data = responseObject.payload;
  return data;
}

export async function createOrder(cartList, total, user){
  const browserData = getSession();
  const order = {
      cartList: cartList,
      amount_paid: total,
      quantity: cartList.length,
      // user: {
      //     name: user.name,
      //     email: user.email,
      //     id: user.id
      // }
      userId: user.id,
  }
  console.log(order)
  const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json", Authorization: `Bearer ${browserData.token}` },
      body: JSON.stringify(order)
  }
  // const response = await fetch(`${host}/660/orders`, requestOptions);
  const response = await fetch(`${host}/orders`, requestOptions);
  if(!response.ok){
    const responseObject = await response.json();
    const errorMessage = responseObject.message? responseObject.message: "Unknown error";
    throw { message: errorMessage, status: response.status }; 
  }
  const responseObject = await response.json();
  const data = responseObject.payload;
  return data;
}