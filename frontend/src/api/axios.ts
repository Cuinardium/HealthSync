import Axios from "axios";

// TODO: mover a .env
const API_URL = "http://server/paw-2023a-02/api";
// const API_URL = 'http://pawserver.it.itba.edu.ar/paw-2023a-02/api'

const user = "cuini123+10@gmail.com";
const password = "1234";
const basic = btoa(user + ":" + password);

export const axios = Axios.create({
  baseURL: API_URL,
  timeout: 10000,
  headers: {
    Authorization: "Basic " + basic,
  },
  paramsSerializer: {
    indexes: null,
  }
});
