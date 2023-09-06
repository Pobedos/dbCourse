import {Table} from "react-bootstrap";
import React from "react";
import UsersItem from "./UsersItem";
import {IUser} from "../../models/IUser";

function UsersList(props: {users: IUser[]}): JSX.Element {
    return (
            <>
                <Table striped bordered hover responsive>
                    <thead>
                    <tr>
                        <th>№</th>
                        <th>Name</th>
                        <th>Role</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        props.users.map((user: IUser, index: number) => {
                            return <UsersItem index={index} user={user} key={user.id}/>
                        })
                    }
                    </tbody>
                </Table>
            </>
        );
}

export default UsersList;