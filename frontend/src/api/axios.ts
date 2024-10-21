import Axios from "axios";

// TODO: mover a .env
const API_URL = "http://server/paw-2023a-02/api";
// const API_URL = 'http://pawserver.it.itba.edu.ar/paw-2023a-02/api'

export const axios = Axios.create({
  baseURL: API_URL,
  timeout: 10000,
  paramsSerializer: {
    indexes: null,
  }
});
