import React, {useContext, useEffect, useState} from 'react';
import {Button} from "react-bootstrap";
import EventList from "../components/Events/UsersList";
import {IEvent} from "../models/Event";
import EventService from "../services/EventService";
import {Link} from "react-router-dom";
import {AuthContext} from "../contexts/AuthContext";
import {Role} from "../store/Role";

const EventsPage = () => {
    const [events, setEvents] = useState<IEvent[]>([]);
    const {store} = useContext(AuthContext)

    async function getEvents() {
        const response = await EventService.fetchEvents()
        console.log(response.data)
        setEvents(response.data)
    }

    useEffect(() => {
        getEvents()
    }, [])

    return (<div>
            <br/>

                <div style={{display: "flex", justifyContent: "space-between"}}>
                    <h2>Events</h2>
                    {
                        store.role === Role.DIRECTOR
                            ?
                    <Link to={"/event/create"}><Button size={'sm'} variant={"success"}>Add Event</Button></Link>
                            :
                            <></>
                    }
                </div>
        <br/>
        {events.length
        ?
            <EventList events={events}/>
            :
            <p>No Events</p>
        }

    </div>);
};

export default EventsPage;
