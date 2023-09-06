import {Table} from "react-bootstrap";
import React from "react";
import IBook from "../../models/IBook";
import PopularBookItem from "./PopularBookItem";

function PopularBooksList(props: {books: IBook[]}): JSX.Element {
    return (
        <>
            <Table striped bordered hover responsive>
                <thead>
                <tr>
                    <th>№</th>
                    <th>Name</th>
                    <th>Authors</th>
                    <th>Count</th>
                </tr>
                </thead>
                <tbody>
                {
                    props.books.map((book: IBook, index: number) => {
                        return <PopularBookItem index={index} book={book} key={book.id}/>
                    })
                }
                </tbody>
            </Table>
        </>
    );
}

export default PopularBooksList;