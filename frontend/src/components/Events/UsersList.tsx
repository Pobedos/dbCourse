import {Table} from "react-bootstrap";
import React from "react";
import {IEvent} from "../../models/Event";
import EventItem from "./EventItem";

function EventList(props: {events: IEvent[]}): JSX.Element {
    return (
            <>
                <Table striped bordered hover responsive>
                    <thead>
                    <tr>
                        <th>№</th>
                        <th>Name</th>
                        <th>Host</th>
                        <th>Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        props.events.map((event: IEvent, index: number) => {
                            return <EventItem index={index} event={event} key={event.id}/>
                        })
                    }
                    </tbody>
                </Table>
            </>
        );
}

export default EventList;