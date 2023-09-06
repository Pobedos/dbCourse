import {AxiosResponse} from "axios";
import $api from "../http";
import IBook from "../models/IBook";
import IAuthor from "../models/IAuthor";
import {IReview} from "../models/IReview";

export default class BookService {
    static fetchBooks(): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>('/books')
    }

    static fetchPopular(): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>('/books/popular')
    }

    static fetchBook(id: string): Promise<AxiosResponse<IBook>> {
        return $api.get<IBook>(`/books/${id}`)
    }

    static fetchUserBooks(id: string): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>(`/books/user/${id}`)
    }

    static fetchUserOrders(id: string): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>(`/books/user/${id}/orders`)
    }

    static fetchAuthors(): Promise<AxiosResponse<IAuthor[]>> {
        return $api.get<IAuthor[]>(`/books/authors`)
    }

    static fetchGenres(): Promise<AxiosResponse<IAuthor[]>> {
        return $api.get<IAuthor[]>(`/books/genres`)
    }

    static fetchStates(): Promise<AxiosResponse<IAuthor[]>> {
        return $api.get<IAuthor[]>(`/books/states`)
    }

    static createBook(name: string, authors: IAuthor[], genre: IAuthor): Promise<AxiosResponse<IAuthor[]>> {
        return $api.post<IAuthor[]>(`/books`, {name: name, authors: authors, genre: genre})
    }

    static addReview(text: string, bookId: string): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/books/reviews`, {text: text, bookId: bookId})
    }

    static addInstance(bookId: string, state: string): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/books/instance`, {id: bookId, instanceId: state})
    }

    static fetchAllOrders(): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>(`/books/orders`)
    }

    static returnBook(id: number): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/books/return`, {id: id});
    }

    static orderBook(id: number, userId: string): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/books/orders`, {id: id, userId: userId});
    }

    static approveOrder(id: number, userId: string): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/books/orders/approve`, {id: id, userId: userId});
    }

    static returnOrder(id: number): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/books/cancel`, {id: id});
    }

    static returnBookToStock(id: number): Promise<AxiosResponse<void>> {
        return $api.post<void>(`/books/stock`, {id: id});
    }

    static fetchBooksByAuthor(id: string): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>(`/books/author/${id}`);
    }

    static fetchAuthorName(id: string): Promise<AxiosResponse<IAuthor>> {
        return $api.get<IAuthor>(`/authors/${id}`);
    }

    static fetchInstances(id: string): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>(`/books/${id}/instances`);
    }

    static fetchReviews(id: string): Promise<AxiosResponse<IReview[]>> {
        return $api.get<IReview[]>(`/books/${id}/reviews`);
    }

    static fetchBooksOnEvent(id: string): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>(`/books/event/` + id);
    }

    static getInstancesInHall(): Promise<AxiosResponse<IBook[]>> {
        return $api.get<IBook[]>(`/books/hall/`);
    }
}