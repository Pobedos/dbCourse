import axios from 'axios';
import {AuthResponse} from "../models/response/AuthResponse";
import {store} from "../contexts/AuthContext";
import {IUser} from "../models/IUser";

export const API_URL = `http://localhost:8080/api`


const $api = axios.create({
    withCredentials: true,
    baseURL: API_URL
})

$api.interceptors.request.use((config) => {
    config.headers!.Authorization = `Bearer ${localStorage.getItem('token')}`
    return config;
})

$api.interceptors.response.use((config) => {
    return config;
},async (error) => {
    const originalRequest = error.config;
    if (error.response.status == 401 && error.config && !error.config._isRetry) {
        originalRequest._isRetry = true;
        try {
            const response = await axios.get<AuthResponse>(`${API_URL}/refresh`, {withCredentials: true})
            localStorage.setItem('token', response.data.accessToken);
            return $api.request(originalRequest);
        } catch (e) {
            localStorage.removeItem('token')
            console.log('НЕ АВТОРИЗОВАН')
            await store.logout()
        }
    }
    throw error;
})

export default $api;
