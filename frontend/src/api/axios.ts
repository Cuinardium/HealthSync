import Axios from "axios";

const API_URL = process.env.REACT_APP_API_URL;

export const axios = Axios.create({
  baseURL: API_URL,
  timeout: 10000,
  paramsSerializer: {
    indexes: null,
  }
});
