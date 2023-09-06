import {AxiosResponse} from "axios";
import $api from "../http";
import {IEvent} from "../models/Event";
import * as buffer from "buffer";

export default class EventService {
    static fetchEvents(): Promise<AxiosResponse<IEvent[]>> {
        return $api.get<IEvent[]>('/events')
    }

    static fetchEvent(id: string): Promise<AxiosResponse<IEvent>> {
        return $api.get<IEvent>('/events/' + id)
    }

    static join(id: number, userid: string): Promise<AxiosResponse<void>> {
        return $api.post<void>('/events/join', {id: id, userId: userid})
    }

    static leave(id: number, userid: string): Promise<AxiosResponse<void>> {
        return $api.post<void>('/events/leave', {id: id, userId: userid})
    }

    static deleteBook(id: number, bookId: number): Promise<AxiosResponse<void>> {
        return $api.post<void>('/events/delete', {id: id, userId: bookId})
    }

    static deleteEvent(id: number): Promise<AxiosResponse<void>> {
        return $api.delete<void>('/events/' + id);
    }

    static createEvent(name: string, books: number[], host: string, time: string) {
        return $api.post<void>('/events', {name: name, books: books, host: host, time: time});
    }
}
