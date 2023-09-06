import React, {useContext, useEffect, useState} from 'react';
import {Link, useParams} from "react-router-dom";
import {AuthContext} from "../contexts/AuthContext";
import IBook from "../models/IBook";
import BookService from "../services/BookService";
import {IUser} from "../models/IUser";
import UserService from "../services/UserService";
import {Role} from "../store/Role";
import {Button} from "react-bootstrap";

const UserPage = () => {
    const {id} = useParams<{ id: string }>()
    const {store} = useContext(AuthContext);
    const [user, setUser] = useState<IUser>({} as IUser)
    const [books, setBooks] = useState<IBook[]>([])

    async function getUser(id: string) {
        const response = await UserService.fetchUser(id)
        console.log(response.data)
        setUser(response.data);
    }

    useEffect(() => {
        if (id === undefined) {
            setUser({} as IUser);
        } else {
            getUser(id)
            getBooks(id)
        }
    }, [id])

    if (user === null) {
        return (<h2>User not found!</h2>)
    }

    async function getBooks(id: string) {
        const response = await BookService.fetchUserBooks(id)
        console.log(response.data)
        setBooks(response.data)
    }

    async function dismiss(id: string) {
        const response = await UserService.dismiss(id)
        console.log(response.data)
        getUser(id)
    }

    return (<div>
        <h2>{user.login}</h2>
        <p>Name: {user.firstName + ' ' + user.lastName}</p>
        <p>Number: {user.number}</p>
        <p>Role: {user.role}</p>
        {
            store.role === Role.DIRECTOR && user.role === 'ROLE_LIBRARIAN'
            ?
                <>
                    <Button variant={"danger"} onClick={() => dismiss(user.id)}>Уволить</Button>
                    <br/>
                    <br/>
                </>
                :<></>
        }
        {books.length ? <>
            <p>{user.login?.charAt(0).toUpperCase() + user.login?.slice(1)}'s books:</p>
            <ol>
                {books.map(book => <li key={book.instanceId}
                                       style={{display: "flex", justifyContent: "space-between", margin: "5px 0"}}>
                    <Link to={"/books/" + book.id}>
                        {book.name}
                    </Link>
                </li>)}
            </ol>
        </> : <></>}
    </div>);
};

export default UserPage;