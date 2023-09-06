import IBook from "../../models/IBook";
import {Link} from "react-router-dom";
import React from "react";

function BookItem(props: { book: IBook, index: number }): JSX.Element {
    return (
        <tr>
            <td>{props.index + 1}</td>
            <td>
                <Link to={"/books/" + props.book.id}>
                    {props.book.name}
                </Link>
            </td>
            <td>{props.book.genre}</td>
            <td>{props.book.authors}</td>
        </tr>
    );
}

export default BookItem;