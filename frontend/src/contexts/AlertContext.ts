import {createContext} from 'react'

export interface IAlertMessage {
    message: string
    show: boolean
}

export interface IAlertContext {
    message: IAlertMessage
    sendMessage: (text: string) => void
    close: () => void
}

export const AlertContext = createContext<IAlertContext | null>(null)
