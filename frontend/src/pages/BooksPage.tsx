import {ChangeEvent, useContext, useEffect, useMemo, useState} from "react";
import IBook from "../models/IBook";
import {AuthContext} from "../contexts/AuthContext";
import BookService from "../services/BookService";
import {observer} from "mobx-react-lite";
import BooksList from "../components/Books/BooksList";
import { Form } from "react-bootstrap";

function BooksPage(): JSX.Element {
    const [books, setBooks] = useState<IBook[]>([]);
    const context = useContext(AuthContext);
    const [query, setQuery] = useState<string>('')

    async function getBooks(): Promise<void> {
        try {
            const response = await BookService.fetchBooks();
            setBooks(response.data);
            console.log(books)
        } catch (e) {
            setBooks([])
            console.log(e);
        }
    }

    useEffect(() => {
        getBooks();
    }, [context.store.isAuth])

    const filteredBooks = useMemo(() => {
        return books.filter(book => book.name.toLowerCase().includes(query.toLowerCase()))
    }, [query, books])

    return (<>
                <br/>
                <h2>Books</h2>
                <br/>
                <Form.Control
                    type={"text"}
                    value={query}
                    onChange={(e: ChangeEvent<HTMLInputElement>) => setQuery(e.target.value)}
                    placeholder={"Search..."}
                />
                <br/>
                <BooksList books={filteredBooks}/>
            </>
    );
}

export default observer(BooksPage);