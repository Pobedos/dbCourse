import {createContext} from 'react'
import Store from "../store/Store";

interface State {
    store: Store,
}
export const store = new Store();

export const AuthContext = createContext<State>({
    store,
})
