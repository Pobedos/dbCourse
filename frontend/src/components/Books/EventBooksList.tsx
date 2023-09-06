import React from 'react';
import IBook from "../../models/IBook";
import {Table} from "react-bootstrap";
import BookItem from "./BookItem";
import EventBookItem from "./EventBookItem";

const EventBooksList = (props: { books: IBook[], onDelete: (id: number) => void }): JSX.Element => {

    return (<>
        <Table striped bordered hover responsive>
            <thead>
            <tr>
                <th>№</th>
                <th>Name</th>
                <th>State</th>
                <th>Remove</th>
            </tr>
            </thead>
            <tbody>
            {props.books.map((book: IBook, index: number) => {
                return <EventBookItem onDelete={props.onDelete} index={index} book={book} key={book.id}/>
            })}
            </tbody>
        </Table>
    </>);
};

export default EventBooksList;