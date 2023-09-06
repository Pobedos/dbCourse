import React, {useContext, useEffect, useState} from 'react';
import {IEvent} from "../models/Event";
import {Link, useNavigate, useParams} from "react-router-dom";
import EventService from "../services/EventService";
import IBook from "../models/IBook";
import {IUser} from "../models/IUser";
import BookService from "../services/BookService";
import UserService from "../services/UserService";
import EventUsersList from "../components/Users/EventUsersList";
import EventBooksList from "../components/Books/EventBooksList";
import {AuthContext} from "../contexts/AuthContext";
import {Button} from "react-bootstrap";
import {Role} from "../store/Role";

const EventPage = () => {
    const {id} = useParams()
    const navigate = useNavigate()
    const {store} = useContext(AuthContext)
    const [event, setEvent] = useState<IEvent>({} as IEvent);
    const [books, setBooks] = useState<IBook[]>([]);
    const [users, setUsers] = useState<IUser[]>([]);

    async function getEvent(id: string) {
        const response = await EventService.fetchEvent(id)
        console.log(response.data)
        setEvent(response.data)
    }

    async function getBooks(id: string) {
        const response = await BookService.fetchBooksOnEvent(id);
        setBooks(response.data)
        console.log(response.data)
    }

    async function getUsers(id: string) {
        const response = await UserService.fetchUsersOnEvent(id);
        setUsers(response.data)
        console.log('users')
        console.log(response.data)
    }

    useEffect(() => {
        if (id !== undefined) {
            getEvent(id)
            getUsers(id)
            getBooks(id)
        }
    }, [id])

    async function join() {
        const response = await EventService.join(event.id, store.user.id);
        if (id !== undefined) {
            setUsers(users => [...users, store.user])
        }
        console.log(response)
        console.log(users)
    }

    async function leave() {
        const response = await EventService.leave(event.id, store.user.id);
        setUsers(users => users.filter(user => user.id != store.user.id))
        console.log(users)
    }

    async function deleteInstance(id: number) {
        const response = EventService.deleteBook(event.id, id);
    setBooks(book => book.filter(book => book.instanceId !== id))
        console.log(response)
    }

    async function deleteEvent() {
        const response = await EventService.deleteEvent(event.id);
        console.log(response)
        navigate('/events')
    }

    return (
        <>
            <br/>
            <p style={{display: 'flex', justifyContent: 'space-between'}}>
                <h2>{event.name}</h2>
                {
                    store.role === Role.DIRECTOR
                    ?
                        <Button size={"sm"} variant={"danger"} onClick={async () => await deleteEvent()}>Delete Event</Button>
                    :
                        <></>
                }
            </p>
            <h4>Organizer:&nbsp;
                <Link to={"/users/" + event.host}>{event.hostName}</Link>
            </h4>
            <h4>Date: {event.date}</h4>
            <h3>Participants on this event: </h3>
            {
                users.length
                    ?
                    <><EventUsersList users={users}/></>
                    :
                    <p>There are no participants on this event</p>
            }
            {
                !users.find(user => user.id === store.user.id)
                ?
                    <><Button variant={"success"} onClick={() => join()}>Join</Button><br/></>
                    :
                    <><Button variant={"warning"} onClick={() => leave()}>Leave</Button><br/></>
            }
            <br/>
            <h3>Books on this event: </h3>
            {
                books.length
                ?
                    <EventBooksList onDelete={(id) => deleteInstance(id)} books={books}/>
                    :
                    <p>There are no books on this event</p>
            }
            {}
        </>
    );
};

export default EventPage;