import React, {useEffect, useState} from 'react';
import {IUser} from "../models/IUser";
import UserService from "../services/UserService";
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";

const RequestsPage = () => {
    const [users, setUsers] = useState<IUser[]>([])

    async function getRequests() {
        const response = await UserService.getRequests()
        console.log(response)
        setUsers(response.data)
    }

    useEffect(() => {
        getRequests()
    }, [])

    async function declineRequest(id: string) {
        const response = await UserService.declineRequest(id)
        console.log(response)
        getRequests()
    }

    async function approveRequest(id: string) {
        const response = await UserService.approveRequest(id)
        console.log(response)
        getRequests()
    }

    return (<div>
            <br/>
            <h2>Work Requests</h2>
            {users.length ? <ol>
                {users.map(user => <li key={user.id}
                                       style={{display: "flex", justifyContent: "space-between", margin: "5px 0"}}>
                    <Link to={"/users/" + user.id}>
                        {user.firstName + ' ' + user.lastName}
                    </Link>
                    <>{user.number}</>
                    <Button variant={"success"} size={"sm"} onClick={() => approveRequest(user.id)}>Approve</Button>
                    <Button variant={"danger"} size={"sm"} onClick={() => declineRequest(user.id)}>Cancel</Button>
                </li>)}
            </ol> : <p>No Requests</p>}
        </div>);
};

export default RequestsPage;