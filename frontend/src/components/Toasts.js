import React, {useContext} from "react";
import {Toast, ToastContainer} from "react-bootstrap";
import {AlertContext} from "../contexts/AlertContext";

const Toasts = () => {
    const context = useContext(AlertContext)

    return (
        <ToastContainer className="p-3" position="bottom-start">
            <Toast bg="danger" onClose={context.close} show={context.message.show}
                   delay={3000} autohide>
                <Toast.Header>
                    <small className="me-auto">Just now</small>
                </Toast.Header>
                <Toast.Body>{context.message.text}</Toast.Body>
            </Toast>
        </ToastContainer>
    )
}

export default Toasts