import React, {ChangeEvent, useEffect, useState} from 'react';
import {Button, Col, FloatingLabel, Form, FormLabel} from "react-bootstrap";
// @ts-ignore
import DropdownMultiselect from "react-multiselect-dropdown-bootstrap";
import Multiselect from 'multiselect-react-dropdown';
import BookService from "../services/BookService";
import IAuthor from "../models/IAuthor";

const CreateBookPage = () => {
    const [name, setName] = useState<string>('')
    const [authors, setAuthors] = useState<{ id: string, label: string }[]>([])
    const [genres, setGenres] = useState<{ id: string, label: string }[]>([])
    const [selectedGenre, setSelectedGenre] = useState<{ id: string, label: string }>({} as { id: string, label: string })
    const [selected, setSelected] = useState<{ id: string, label: string }[]>([])

    useEffect(() => {
        getAuthors()
        getGenres()
    }, [])

    async function getAuthors() {
        const response = await BookService.fetchAuthors();
        console.log(response.data);
        setAuthors(response.data.map(author => {
            return {id: author.id, label: author.name} as { id: string, label: string }
        }))
        console.log(authors);
    }

    async function getGenres() {
        const response = await BookService.fetchGenres();
        console.log(response.data);
        setGenres(response.data.map(genre => {
            return {id: genre.id, label: genre.name} as { id: string, label: string }
        }))
        console.log(genres);
    }

    function createBook() {
        console.log(selected)
        console.log(selectedGenre)
        BookService.createBook(name, selected.map(author => { return {id: author.id, name: author.label} as IAuthor }), {id: selectedGenre.id, name: selectedGenre.label})
    }

    return (<div>
        <br/>
        <h2>Create Book</h2>
        <br/>
        <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label
                controlId="floatingInput"
                label="Book name"
                className="mb-3"
            >Book Name</Form.Label>
            <Form.Control type="text"
                          placeholder="Book Name"
                          value={name}
                          onChange={e => setName(e.target.value)}
                          name="Name"/>
        </Form.Group>
        <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label
                controlId="floatingInput"
                label="Book name"
                className="mb-3"
            >Authors</Form.Label>
            <Multiselect options={authors} displayValue="label"
                         onSelect={selectedList => setSelected(selectedList)}
                         onRemove={selectedList => setSelected(selectedList)}/>
        </Form.Group>
        <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label
                controlId="floatingInput"
                label="Book name"
                className="mb-3"
            >Genre</Form.Label>
            <Multiselect options={genres} displayValue="label"
                         onSelect={(selectedList, selectedItem) => setSelectedGenre(selectedItem)}
                         singleSelect/>
        </Form.Group>
        <Button variant={"success"} onClick={() => createBook()}>Create</Button>
    </div>);
};

export default CreateBookPage;