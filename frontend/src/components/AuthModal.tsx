import {Button, FloatingLabel, Form, ModalBody, ModalFooter} from "react-bootstrap";
import React, {useContext, useState} from "react";
import MyModal from "./MyModal";
import {AuthContext} from "../contexts/AuthContext";

interface IAuthModal {
    close: () => void
}

function AuthModal({close}: IAuthModal) {
    const [login, setLogin] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const {store} = useContext(AuthContext);

    return (
        <MyModal title="Login">
            <ModalBody>
                <FloatingLabel
                    controlId="floatingInput"
                    label="Username"
                    className="mb-3"
                >
                    <Form.Control type="text"
                                  placeholder="Username"
                                  value={login}
                                  onChange={e => setLogin(e.target.value)}
                                  name="username"/>
                </FloatingLabel>
                <FloatingLabel controlId="floatingPassword" label="Password">
                    <Form.Control type="password"
                                  placeholder="Password"
                                  value={password}
                                  onChange={e => setPassword(e.target.value)}
                                  name="password"/>
                </FloatingLabel>
            </ModalBody>
            <ModalFooter>
                <Button variant="primary" onClick={() => {
                    store.login(login, password).then(() => close());
                }}>Login</Button>
            </ModalFooter>
        </MyModal>
    )
}

export default AuthModal;
