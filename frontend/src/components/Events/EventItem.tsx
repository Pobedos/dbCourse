import {Link} from "react-router-dom";
import React from "react";
import {IEvent} from "../../models/Event";

function EventItem(props: { event: IEvent, index: number }): JSX.Element {
    return (
        <tr>
            <td>{props.index + 1}</td>
            <td>
                <Link to={"/events/" + props.event.id}>
                    {props.event.name}
                </Link>
            </td>
            <td>{props.event.hostName}</td>
            <td>{props.event.date}</td>
        </tr>
    );
}

export default EventItem;