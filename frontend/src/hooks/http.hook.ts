import {useCallback, useState} from "react";


export function useHttp() {
    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>('')

    const request = useCallback(async (url: string, method = 'GET', body = '', headers: HeadersInit) => {
        setLoading(true)
        try {
            if (body) {
                body = JSON.stringify(body)
                // @ts-ignore
                headers['Content-Type'] = 'application/json'
            }
            const response = await fetch(url, {method, body, headers})
            let data = {};
            if (!response.ok) {
                switch (response.status) {
                    case 401: {
                        throw new Error('Login needed')
                    }
                    default: {
                        data = await response.json()
                        // @ts-ignore
                        throw new Error(data.errors ? data.errors[0].defaultMessage : data.message)
                    }
                }
            }
            if (response.status !== 204) {
                console.log(response)
                data = await response.json();
            }
            setLoading(false)
            return data
        } catch (e: any) {
            setLoading(false)
            setError(e.message)
            throw e
        }
    }, [])

    const clearError = useCallback(() => setError(''), [])

    return { loading, request, error, clearError }
}