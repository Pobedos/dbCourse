import React from 'react';
import {Link} from "react-router-dom";
import {IUser} from "../../models/IUser";

const EventUserItem = (props: { user: IUser, index: number }) => {
    return (
        <tr>
            <td>{props.index + 1}</td>
            <td>
                <Link to={"/users/" + props.user.id}>
                    {props.user.firstName + ' ' + props.user.lastName}
                </Link>
            </td>
            <td>{props.user.login}</td>
        </tr>
    );
};

export default EventUserItem;