import IBook from "../../models/IBook";
import {Link} from "react-router-dom";
import React from "react";

function PopularBookItem(props: { book: IBook, index: number }): JSX.Element {
    return (
        <tr>
            <td>{props.index + 1}</td>
            <td>
                <Link to={"/books/" + props.book.id}>
                    {props.book.name}
                </Link>
            </td>
            <td>{props.book.authorsString}</td>
            <td>{props.book.count}</td>
        </tr>
    );
}

export default PopularBookItem;