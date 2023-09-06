import {IUser} from "../models/IUser";
import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";
import axios from 'axios';
import {AuthResponse} from "../models/response/AuthResponse";
import {API_URL} from "../http";
import {fromUser, Role} from "./Role";

export default class Store {
    user = {} as IUser;
    isAuth = false;
    isLoading = false;
    role: Role = {} as Role

    constructor() {
        makeAutoObservable(this);
    }

    setAuth(bool: boolean) {
        this.isAuth = bool;
    }

    setUser(user: IUser) {
        this.user = user;
    }

    setRole(role: Role) {
        this.role = role;
    }

    setLoading(bool: boolean) {
        this.isLoading = bool;
    }

    async login(login: string, password: string) {
        try {
            const response = await AuthService.login(login, password);
            console.log(response)
            localStorage.setItem('token', response.data.accessToken);
            this.setAuth(true);
            this.setUser(response.data.user);
            this.setRole(fromUser(response.data.user))
        } catch (e: any) {
            alert(e.response?.data?.message)
            throw e;
        }
    }

    async registration(login: string, password: string) {
        try {
            const response = await AuthService.registration(login, password);
            console.log(response)
            localStorage.setItem('token', response.data.accessToken);
            this.setAuth(true);
            this.setUser(response.data.user);
        } catch (e: any) {
            console.log(e.response?.data?.message);
        }
    }

    async logout() {
        try {
            const response = await AuthService.logout();
            localStorage.removeItem('token');
            this.setAuth(false);
            this.setUser({} as IUser);
            this.setRole({} as Role)
            console.log(this.isAuth)
        } catch (e: any) {
            console.log(e.response?.data?.message);
        }
    }

    async checkAuth() {
        this.setLoading(true);
        try {
            const response = await axios.get<AuthResponse>(`${API_URL}/refresh`, {withCredentials: true})
            console.log('check auth');
            localStorage.setItem('token', response.data.accessToken);
            this.setAuth(true);
            this.setUser(response.data.user);
        } catch (e: any) {
            console.log(e.response?.data?.message);
        } finally {
            this.setLoading(false);
        }
    }
}