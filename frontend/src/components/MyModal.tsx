import React, {useContext} from "react";
import {Modal} from "react-bootstrap";
import {ModalContext} from "../contexts/ModalContext";

export interface ModalProps {
    children: React.ReactNode
    title: string
}

function MyModal({ children, title }: ModalProps) {
    const {open, close, modal} = useContext(ModalContext)

    return (
        <>
            <Modal show={modal} onHide={close}>
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                { children }
            </Modal>
        </>)
}

export default MyModal;