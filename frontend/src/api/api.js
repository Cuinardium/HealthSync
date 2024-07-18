import axios from 'axios'

const API_URL = 'http://localhost:8080/api'
// const API_URL = 'http://pawserver.it.itba.edu.ar/paw-2023a-02/api'

const api = axios.create({
    baseURL: API_URL,
    timeout: 1000
});

export default api;