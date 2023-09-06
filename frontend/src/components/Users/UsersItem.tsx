import {Link} from "react-router-dom";
import React from "react";
import {IUser} from "../../models/IUser";

function UsersItem(props: { user: IUser, index: number }): JSX.Element {
    return (
        <tr>
            <td>{props.index + 1}</td>
            <td>
                <Link to={"/users/" + props.user.id}>
                    {props.user.firstName + ' ' + props.user.lastName}
                </Link>
            </td>
            <td>{props.user.role}</td>
        </tr>
    );
}

export default UsersItem;