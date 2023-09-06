import {useCallback, useEffect, useState} from 'react'
import {useCookies} from "react-cookie";

const storageName = 'token'
const expirationTime = 60000

export interface IAuthHook {
    login: (jwtToken: string) => void
    logout: () => void
    cookies: { token?: any }
    ready: boolean
}

export function useAuth(): IAuthHook {
    const [token, setToken] = useState<string>('')
    const [ready, setReady] = useState(false)
    const [cookies, setCookies, removeCookies] = useCookies([storageName]);

    const login = useCallback((jwtToken: string) => {
        setToken(jwtToken)
        let expires = new Date()
        expires.setTime(expires.getTime() + expirationTime)
        setCookies(storageName, jwtToken, {expires, path: "/"})
    }, [])


    const logout = useCallback(() => {
        setToken('')
        removeCookies(storageName, {path: "/"})
    }, [])

    useEffect(() => {
        if (cookies.token) {
            login(cookies.token)
        }
        setReady(true)
    }, [login, logout])


    return { login, logout, cookies, ready }
}