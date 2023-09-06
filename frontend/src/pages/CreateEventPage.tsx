import React, {useEffect, useState} from 'react';
import {Button, Form} from "react-bootstrap";
import Multiselect from "multiselect-react-dropdown";
import IBook from "../models/IBook";
import {IUser} from "../models/IUser";
import UserService from "../services/UserService";
import BookService from "../services/BookService";
import EventService from "../services/EventService";
import IAuthor from "../models/IAuthor";
import {useNavigate} from "react-router-dom";

const CreateEventPage = () => {
    const [name, setName] = useState<string>('');
    const [books, setBooks] = useState<IBook[]>([]);
    const [selectedUser, setSelectedUser] = useState<IUser>({} as IUser);
    const [users, setUsers] = useState<IUser[]>([]);
    const [selectedBooks, setSelectedBooks] = useState<IBook[]>([]);
    const [time, setTime] = useState<string>('')
    const navigate = useNavigate();

    async function getUsers() {
        const response = await UserService.getLibrarians();
        setUsers(response.data);
        console.log(response)
    }

    async function getBooks() {
        const response = await BookService.getInstancesInHall();
        setBooks(response.data);
        console.log(response)
    }

    async function createEvent() {
        const response = await EventService.createEvent(name, selectedBooks.map(book => { return book.instanceId }), selectedUser.id, time);
        navigate('/events')
    }

    useEffect(() => {
        getUsers()
        getBooks()
    }, [])

    return (<div>
            <br/>
            <h2>Create Event</h2>
            <br/>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label
                    controlId="floatingInput"
                    label="Event name"
                    className="mb-3"
                >Event Name</Form.Label>
                <Form.Control type="text"
                              placeholder="Event Name"
                              value={name}
                              onChange={e => setName(e.target.value)}
                              name="Name"/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label
                    controlId="floatingInput"
                    label="Books"
                    className="mb-3"
                >Books</Form.Label>
                <Multiselect options={books} displayValue="name"
                             onSelect={selectedList => setSelectedBooks(selectedList)}
                             onRemove={selectedList => setSelectedBooks(selectedList)}/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label
                    controlId="floatingInput"
                    label="Organizer"
                    className="mb-3"
                >Organizer</Form.Label>
                <Multiselect options={users} displayValue="login"
                             onSelect={(selectedList, selectedItem) => setSelectedUser(selectedItem)}
                             onRemove={(selectedList, selectedItem) => setSelectedUser(selectedItem)} singleSelect/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label
                    controlId="floatingInput"
                    label="Organizer"
                    className="mb-3"
                >Date</Form.Label>
                <Form.Control type={"datetime-local"} value={time} onChange={e => setTime(e.target.value)}></Form.Control>
            </Form.Group>
            <Button variant={"success"} onClick={() => createEvent()}>Create</Button>
        </div>
    );
};

export default CreateEventPage;