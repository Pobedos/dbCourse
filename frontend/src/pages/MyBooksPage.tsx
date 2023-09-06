import React, {useContext, useEffect, useState} from 'react';
import BookService from "../services/BookService";
import {AuthContext} from "../contexts/AuthContext";
import IBook from "../models/IBook";
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";

const MyBooksPage = () => {
    const {store} = useContext(AuthContext)
    const [books, setBooks] = useState<IBook[]>([])

    async function getBooks() {
        const response = await BookService.fetchUserBooks(store.user.id)
        console.log(response.data)
        setBooks(response.data)
    }

    useEffect(() => {
        getBooks()
    }, [])

    async function returnBook(id: number) {
        console.log(id)
        await BookService.returnBook(id)
        setBooks(books => books.filter(book => book.instanceId !== id))
    }

    return (<div>
        <br/>
        <h2>My in hand books</h2>
        {books.length ? <ol>
            {books.map(book => <li key={book.instanceId}
                                   style={{display: "flex", justifyContent: "space-between", margin: "5px 0"}}>
                <Link to={"/books/" + book.id}>
                    {book.name}
                </Link>
                <Button size={"sm"} onClick={() => returnBook(book.instanceId)}>Return this</Button>
            </li>)}
        </ol> : <p>No Books</p>}

    </div>);
};

export default MyBooksPage;