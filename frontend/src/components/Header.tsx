import React, {useContext} from "react";
import {Button, Container, Nav, Navbar} from "react-bootstrap";
import {Link} from "react-router-dom";
import {AuthContext} from "../contexts/AuthContext";
import {ModalContext} from "../contexts/ModalContext";
import AuthModal from "./AuthModal";
import {observer} from "mobx-react-lite";
import {Role} from "../store/Role";
import UserService from "../services/UserService";

const Header = () => {
    const {store} = useContext(AuthContext)
    const {open, close, modal} = useContext(ModalContext)

    async function sendRequest() {
        if (store.role === Role.CLIENT) {
            const response = UserService.sendRequest(store.user.id)
            store.setUser({...store.user, request: true})
            console.log(response)
        }
    }

    return (<>
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
                <Navbar.Brand>
                    <Link className="navbar-brand" to="/">
                        Library
                    </Link>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Item>
                            <Link className="nav-link" to="/books">Books</Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Link className="nav-link" to="/my-books">My Books</Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Link className="nav-link" to="/orders">Orders</Link>
                        </Nav.Item>
                        {store.role === Role.DIRECTOR || store.role === Role.CLIENT || store.role === Role.LIBRARIAN
                        ?
                            <Nav.Item>
                                <Link className="nav-link" to="/events">Events</Link>
                            </Nav.Item>
                            :
                            <></>
                        }
                        {store.role === Role.LIBRARIAN ? <Nav.Item>
                            <Link className="nav-link" to="/books/create">Add Book</Link>
                        </Nav.Item> : <></>}
                        {store.role === Role.DIRECTOR ? <>
                            <Nav.Item>
                                <Link className="nav-link" to="/users">Users</Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Link className="nav-link" to="/requests">Requests</Link>
                            </Nav.Item>
                        </> : <></>}
                    </Nav>
                    {!store.isAuth ? <Nav>
                        <Button variant="primary" onClick={open}>Login</Button>
                    </Nav> : <Nav>
                        {store.role === Role.CLIENT && !store.user.request ? <Nav.Item>
                            <Button variant={"secondary"} onClick={() => sendRequest()}>Work Request</Button>
                        </Nav.Item> : <></>}
                        <Nav.Item>
                            <Nav.Link style={{color: "white"}} eventKey="disabled" disabled>
                                {store.user?.role.replace('ROLE_', '')}
                            </Nav.Link>
                        </Nav.Item>
                        <Button variant="secondary" onClick={() => store.logout()}>Logout</Button>
                    </Nav>}
                </Navbar.Collapse>
            </Container>
        </Navbar>
        {modal && <AuthModal close={close}/>}
    </>);
}

export default observer(Header);
