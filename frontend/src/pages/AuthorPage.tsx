import React, {useEffect, useState} from 'react';
import IBook from "../models/IBook";
import $api from "../http";
import BookService from "../services/BookService";
import {useNavigate, useParams} from "react-router-dom";
import IAuthor from "../models/IAuthor";
import AuthorBooksList from "../components/Authors/AuthorBooksList";

const AuthorPage = () => {
    const {id} = useParams<{ id: string }>()
    const [books, setBooks] = useState<IBook[]>([])
    const [author, setAuthor] = useState<IAuthor | null>(null);
    const navigate = useNavigate();

    async function getBooks(id: string) {
        const response = await BookService.fetchBooksByAuthor(id)
        console.log(response.data)
        setBooks(response.data);
    }

    async function getAuthor(id: string) {
        const response = await BookService.fetchAuthorName(id)
        if (response.status == 404) {
            return navigate("/");
        }
        console.log(response.data)
        setAuthor(response.data);
    }

    // @ts-ignore
    useEffect(() => {
        if (id === undefined) {
            setBooks([]);
        } else {
            getBooks(id)
        }
        if (id != null) {
            getAuthor(id)
        } else {
            return navigate("/");
        }

    }, [])


    return (
        <div>
            <h2>{author?.name}</h2>
            <br/>
            <AuthorBooksList books={books}/>
        </div>
    );
};

export default AuthorPage;