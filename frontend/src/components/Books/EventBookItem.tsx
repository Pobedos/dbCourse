import React from 'react';
import IBook from "../../models/IBook";
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";

const EventBookItem = (props: { book: IBook, index: number, onDelete: (id: number) => void }): JSX.Element => {
    return (
        <tr>
            <td>{props.index + 1}</td>
            <td>
                <Link to={"/books/" + props.book.id}>
                    {props.book.name}
                </Link>
            </td>
            <td>{props.book.state}</td>
            <td><Button size={"sm"} variant={"outline-danger"} onClick={() => props.onDelete(props.book.instanceId)}>Remove</Button></td>
        </tr>
    );
};

export default EventBookItem;