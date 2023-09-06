import React, {ChangeEvent, useContext, useEffect, useMemo, useState} from 'react';
import {Form} from "react-bootstrap";
import {AuthContext} from "../contexts/AuthContext";
import {IUser} from "../models/IUser";
import UserService from "../services/UserService";
import UsersList from "../components/Users/UsersList";

const UsersPage = () => {
    const [users, setUsers] = useState<IUser[]>([]);
    const context = useContext(AuthContext);
    const [query, setQuery] = useState<string>('')

    async function getUsers() {
        try {
            const response = await UserService.fetchUsers();
            setUsers(response.data);
            console.log(users)
        } catch (e) {
            setUsers([])
            console.log(e);
        }
    }

    useEffect(() => {
        getUsers();
    }, [])

    const filteredUsers = useMemo(() => {
        console.log(users.filter(user => user.firstName.concat(' ').concat(user.lastName).toLowerCase().includes(query.toLowerCase())))
        return users.filter(user => user.firstName.concat(' ').concat(user.lastName).toLowerCase().includes(query.toLowerCase()))
    }, [query, users])

    return (
        <div>
            <br/>
            <h2>Users</h2>
            <br/>
            <Form.Control
                type={"text"}
                value={query}
                onChange={(e: ChangeEvent<HTMLInputElement>) => setQuery(e.target.value)}
                placeholder={"Search..."}
            />
            <br/>
            <UsersList users={filteredUsers}/>
        </div>
    );
};

export default UsersPage;