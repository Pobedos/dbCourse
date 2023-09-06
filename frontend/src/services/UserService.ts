import $api from "../http";
import {AxiosResponse} from 'axios';
import {IUser} from "../models/IUser";

export default class UserService {
    static fetchUsers(): Promise<AxiosResponse<IUser[]>> {
        return $api.get<IUser[]>('/users')
    }

    static getLibrarians(): Promise<AxiosResponse<IUser[]>> {
        return $api.get<IUser[]>('/users/lib')
    }

    static fetchUser(id: string): Promise<AxiosResponse<IUser>> {
        return $api.get<IUser>(`/users/${id}`)
    }

    static sendRequest(id: string): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/users/work`, {id: id})
    }

    static getRequests(): Promise<AxiosResponse<IUser[]>> {
        return $api.get<IUser[]>(`/users/requests`)
    }

    static fetchUsersOnEvent(id: string): Promise<AxiosResponse<IUser[]>> {
        return $api.get<IUser[]>(`/users/event/` + id)
    }

    static declineRequest(id: string): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/users/decline`, {id: id})
    }

    static dismiss(id: string): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/users/dismiss`, {id: id})
    }

    static approveRequest(id: string): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/users/approve`, {id: id})
    }
}

