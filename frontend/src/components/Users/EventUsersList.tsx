import React from 'react';
import {Table} from "react-bootstrap";
import {IUser} from "../../models/IUser";
import UsersItem from "./UsersItem";
import EventUserItem from "./EventUserItem";

const EventUsersList = (props: {users: IUser[]}) => {
    return (
        <>
            <Table striped bordered hover responsive>
                <thead>
                <tr>
                    <th>№</th>
                    <th>Name</th>
                    <th>Login</th>
                </tr>
                </thead>
                <tbody>
                {
                    props.users.map((user: IUser, index: number) => {
                        return <EventUserItem index={index} user={user} key={user.id}/>
                    })
                }
                </tbody>
            </Table>
        </>
    );
};

export default EventUsersList;