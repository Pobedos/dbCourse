import {Table} from "react-bootstrap";
import React from "react";
import IBook from "../../models/IBook";
import AuthorBookItem from "./AuthorBookItem";

function AuthorBooksList(props: {books: IBook[]}): JSX.Element {
    return (
            <>
                <Table striped bordered hover responsive>
                    <thead>
                    <tr>
                        <th>№</th>
                        <th>Name</th>
                        <th>Genre</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        props.books.map((book: IBook, index: number) => {
                            return <AuthorBookItem index={index} book={book} key={book.id}/>
                        })
                    }
                    </tbody>
                </Table>
            </>
        );
}

export default AuthorBooksList;