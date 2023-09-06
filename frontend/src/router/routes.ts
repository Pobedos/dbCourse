import {Component, FC, ReactNode} from "react";
import MainPage from "../pages/MainPage";
import BooksPage from "../pages/BooksPage";
import BookPage from "../pages/BookPage";
import AuthorPage from "../pages/AuthorPage";
import MyBooksPage from "../pages/MyBooksPage";
import OrdersPage from "../pages/OrdersPage";
import CreateBookPage from "../pages/CreateBookPage";
import UsersPage from "../pages/UsersPage";
import UserPage from "../pages/UserPage";
import RequestsPage from "../pages/RequestsPage";
import EventsPage from "../pages/EventsPage";
import EventPage from "../pages/EventPage";
import CreateEventPage from "../pages/CreateEventPage";

export interface IRoute {
    path: string
    Component: () => JSX.Element
}

export const publicRoutes: IRoute[] = [
    {path: '', Component: MainPage},
]

export const clientRoutes: IRoute[] = [
    ...publicRoutes,
    {path: '/books', Component: BooksPage},
    {path: '/books/:id', Component: BookPage},
    {path: '/authors/:id', Component: AuthorPage},
    {path: '/my-books', Component: MyBooksPage},
    {path: '/orders', Component: OrdersPage},
    {path: '/events', Component: EventsPage},
    {path: '/events/:id', Component: EventPage},
]

export const librarianRoutes: IRoute[] = [
    ...clientRoutes,
    {path: '/books/create', Component: CreateBookPage},
    {path: '/users', Component: UsersPage},
    {path: '/users/:id', Component: UserPage},
]

export const directorRoutes: IRoute[] = [
    ...librarianRoutes,
    {path: '/requests', Component: RequestsPage},
    {path: '/event/create', Component: CreateEventPage},
]

export const routes = new Map<string | undefined, IRoute[]>([
    ['ROLE_CLIENT', clientRoutes],
    ["ROLE_LIBRARIAN", librarianRoutes],
    ["ROLE_DIRECTOR", directorRoutes],
    [undefined, publicRoutes]
])

