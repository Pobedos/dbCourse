import React, {useEffect, useState} from 'react';
import Header from "./components/Header";
import {Container} from "react-bootstrap";
import Toasts from "./components/Toasts";
import {AlertContext, IAlertContext, IAlertMessage} from "./contexts/AlertContext";
import {AuthContext, store} from "./contexts/AuthContext";
import {observer} from "mobx-react-lite";
import {useRoutes} from "./router";
import {useLocation} from "react-router-dom";

function App(): JSX.Element {
    const routes = useRoutes()
    const location = useLocation()
    const [message, setMessage] = useState<IAlertMessage>({
        show: false, message: ''
    })

    useEffect(() => {
        if (localStorage.getItem('token')) {
            store.checkAuth()
        }
    }, [location])

    const closeAlert = () => {
        setMessage({
            show: false, message: ''
        })
    }

    const sendMessage = (text: string) => {
        setMessage({show: true, message: text})
    }

    const alertContextValue: IAlertContext = {
        message, close: closeAlert, sendMessage
    }

    return (<AuthContext.Provider value={{store}}>
        <AlertContext.Provider value={alertContextValue}>
            <Header/>
            <Container>
                {routes}
            </Container>
            <Toasts/>
        </AlertContext.Provider>
    </AuthContext.Provider>);
}

export default observer(App);
