import React, {useContext, useEffect, useState} from 'react';
import {AuthContext} from "../contexts/AuthContext";
import {Role} from "../store/Role";
import {observer} from "mobx-react-lite";
import IBook from "../models/IBook";
import BookService from "../services/BookService";
import {Link} from "react-router-dom";
import {Button, Table} from "react-bootstrap";
import PopularBookItem from "../components/Books/PopularBookItem";

const OrdersPage = () => {
    const {store} = useContext(AuthContext)
    const [orders, setOrders] = useState<IBook[]>([])

    async function getOrders() {
        if (store.role !== Role.CLIENT) {
            const response = await BookService.fetchAllOrders()
            setOrders(response.data)
            console.log(response.data)
        } else {
            const response = await BookService.fetchUserOrders(store.user.id)
            setOrders(response.data)
            console.log(response.data)
        }
    }

    useEffect(() => {
        getOrders()
    }, [store.user])

    function cancelOrder(id: number) {
        BookService.returnOrder(id);
        setOrders(books => books.filter(book => book.instanceId !== id))
    }

    if (store.role === Role.CLIENT) {
        return (
            <>
                <h2>Your orders</h2>
                {orders.length ? <ol>
                    {orders.map(book => <li key={book.instanceId}
                                           style={{display: "flex", justifyContent: "space-between", margin: "5px 0"}}>
                        <Link to={"/books/" + book.id}>
                            {book.name}
                        </Link>
                        <Button size={"sm"} onClick={() => cancelOrder(book.instanceId)}>Cancel</Button>
                    </li>)}
                </ol> : <p>No Orders</p>}
            </>
        )
    }

    function approveOrder(bookId: number, userId: string) {
        BookService.approveOrder(bookId, userId);
        setOrders(books => books.filter(book => book.instanceId !== bookId))
    }

    return (
        <div>
            <h2>Orders that need approve:</h2>
            {orders.length ?
                <Table borderless hover responsive>
                    <thead>
                    <tr>
                        <th>№</th>
                        <th>Name</th>
                        <th>State</th>
                        <th>User</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        orders.map((book, index) =>
                            <>
                                <tr>
                                    <td>{index + 1}</td>
                                    <td>
                                        <Link to={"/books/" + book.id}>{book.name}</Link>
                                    </td>
                                    <td>{book.state}</td>
                                    <td>
                                        {book.userName}
                                    </td>
                                    <td>
                                        <Button variant={"success"} size={"sm"} onClick={() => approveOrder(book.instanceId, book.userId)}>Approve</Button>
                                    </td>
                                    <td>
                                        <Button variant={"danger"} size={"sm"} onClick={() => cancelOrder(book.instanceId)}>Cancel</Button>
                                    </td>
                                </tr>
                            </>
                        )
                    }
                    </tbody>
                </Table>
                : <p>No Orders</p>}
        </div>
    );
};

export default observer(OrdersPage);