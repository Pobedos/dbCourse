import BookService from "../services/BookService";
import {useContext, useEffect, useState} from "react";
import IBook from "../models/IBook";
import {AuthContext} from "../contexts/AuthContext";
import {observer} from "mobx-react-lite";
import PopularBooksList from "../components/Books/PopularBooksList";

function MainPage(): JSX.Element {
    const [books, setBooks] = useState<IBook[]>([]);
    const context = useContext(AuthContext);

    async function getBooks(): Promise<void> {
        try {
            const response = await BookService.fetchPopular();
            setBooks(response.data);
        } catch (e) {
            setBooks([])
            console.log(e);
        }
    }

    useEffect(() => {
        getBooks();
    }, [context.store.isAuth])

    return (<>
        {context.store.isAuth ? <h1>Welcome {context.store.user.firstName} {context.store.user.lastName}</h1> : <div>
            <h1>Welcome Guest</h1>
        </div>}
        {books.length !== 0 ? <h3>Most popular books</h3> : <p></p>}
        {<PopularBooksList books={books}/>}
    </>);
}

export default observer(MainPage);
