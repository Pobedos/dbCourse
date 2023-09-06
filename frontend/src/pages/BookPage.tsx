import React, {useContext, useEffect, useState} from 'react';
import {Link, useParams} from "react-router-dom";
import BookService from "../services/BookService";
import IBook from "../models/IBook";
import {Button, Form} from "react-bootstrap";
import {AuthContext} from "../contexts/AuthContext";
import {observer} from "mobx-react-lite";
import {IReview} from "../models/IReview";
import {Role} from "../store/Role";
import Multiselect from "multiselect-react-dropdown";
import IAuthor from "../models/IAuthor";

const BookPage = () => {
    const {id} = useParams<{ id: string }>()
    const {store} = useContext(AuthContext);
    const [book, setBook] = useState<IBook | null>(null)
    const [instances, setInstances] = useState<IBook[]>([]);
    const [reviews, setReviews] = useState<IReview[]>([]);
    const [text, setText] = useState<string>('');
    const [states, setStates] = useState<IAuthor[]>([]);
    const [selectedState, setSelectedState] = useState<IAuthor>({} as IAuthor);

    async function getBook(id: string) {
        const response = await BookService.fetchBook(id)
        console.log(response.data)
        setBook(response.data);
    }

    async function getInstances(id: string) {
        const response = await BookService.fetchInstances(id)
        console.log(response.data)
        setInstances(response.data);
    }

    async function getReviews(id: string) {
        const response = await BookService.fetchReviews(id)
        console.log(response.data)
        setReviews(response.data);
    }

    async function getStates() {
        const response = await BookService.fetchStates()
        console.log(response.data)
        setStates(response.data);
    }

    useEffect(() => {
        console.log('book')
        getStates()
        if (id === undefined) {
            setBook(null);
            setInstances([])
        } else {
            getBook(id)
            getReviews(id)
            if (store.user.role === 'ROLE_LIBRARIAN') {
                getInstances(id)
            } else {
                setInstances([])
            }
        }
    }, [id, store.user.role])

    if (book === null) {
        return (<h2>Book not found!</h2>)
    }

    async function takeBook(id: number) {
        BookService.orderBook(id, store.user.id)
            .catch(r => alert(r.response.data.message))
    }

    async function returnBookToStock(id: number) {
        console.log(id)
        await BookService.returnBookToStock(id)
        console.log(instances)
        setInstances(i => i.filter(book => book.instanceId !== id))
    }

    async function addReview() {
        if (id !== undefined) {
            const response = await BookService.addReview(text, id)
            // alert('Review has been added')
            getReviews(id)
            console.log(response.data)
        }
    }

    async function addInstance() {
        if (id !== undefined) {
            console.log(id)
            const response = await BookService.addInstance(id, selectedState.id)
            // alert('Review has been added')
            getInstances(id)
            console.log(response.data)
        }
    }

    return (<div>
        <h2>{book?.name}</h2>
        <p>Author(s): {book?.authors?.map((author, index) => <Link key={index}
                                                                   to={"/authors/" + book?.authorsId.at(index)?.trimStart()}>{author}</Link>)}</p>
        <p>Genre: {book?.genre}</p>
        <p>In hand count: {book?.count}</p>
        {store.role === Role.CLIENT
        ?
            <Button onClick={() => takeBook(book?.id)}>Take</Button>
        :
            <>
                <br/>
                <br/>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label
                        controlId="floatingInput"
                        label="Add Instance"
                        className="mb-3"
                    >Book Name</Form.Label>
                    <Multiselect options={states} displayValue="name"
                                 onSelect={(selectedList, selectedItem) => setSelectedState(selectedItem)}
                                 singleSelect/>
                </Form.Group>
                <Button variant={"success"} onClick={() => addInstance()}>Add</Button>
                <br/>
                <br/>
            </>
        }
        {instances.length ? <p>
            <h3>Instances in hall: </h3>
            <ol>
                {instances.map(i => <li style={{margin: "5px 0"}}>
                    {i.state} <Button variant={"danger"} onClick={async () => await returnBookToStock(i.instanceId)}>In Stock</Button>
                </li>)}
            </ol>
        </p> : <></>}
        {
            store.role === Role.CLIENT
            ?
                <p>
                    <br/>
                    <br/>
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label
                            controlId="floatingInput"
                            label="Add review"
                            className="mb-3"
                        >Book Name</Form.Label>
                        <Form.Control as="textarea"
                                      placeholder="Review"
                                      value={text}
                                      onChange={e => setText(e.target.value)}
                                      name="Name"/>
                    </Form.Group>
                    <Button variant={"success"} onClick={() => addReview()}>Add</Button>
                </p>
                :
                <></>
        }
        <br/>
        <br/>
        <br/>
        <h3>Reviews: </h3>
        {
            !reviews.length
            ?
                <p>No reviews</p>
                :
            reviews.map(r => <>
                <p>Review from <Link to={"/users/" + r.userId}>{r.username}</Link></p>
                <p>{'>'} {r.text}</p>
            </>)
        }
    </div>);
};

export default observer(BookPage);