let API_BASE_URL = "";

const IP = "192.168.88.14";
const backendProjectName = "galana-ejb";

// init the API base URL
const initializeIP = () => {
  API_BASE_URL = `http://${IP}:8080/${backendProjectName}/api`;
};

export const getIP = () => {
  return IP;
};

initializeIP();

// add all API ENDPOINTS here
export const API_ENDPOINTS = {
  prelevement_lubrifiants: `${API_BASE_URL}/prelevement_lubrifiants`,
  encaissement_lubrifiants: `${API_BASE_URL}/encaissement_lubrifiants`,
  vente_products: `${API_BASE_URL}/vente_products`,
  stock_remaining: `${API_BASE_URL}/stock_remaining`
};