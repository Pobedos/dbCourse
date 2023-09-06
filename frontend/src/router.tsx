import React, {useContext, useEffect, useState} from "react";
import {Navigate, Route, Routes} from "react-router-dom";
import {AuthContext, store} from "./contexts/AuthContext";
import {IRoute, publicRoutes, routes} from "./router/routes";
import {IUser} from "./models/IUser";

export const useRoutes = () => {
    const {store} = useContext(AuthContext)

    return (
        <Routes>
            {routes.get(store.user?.role)?.map(route =>
                <Route
                    key={route.path}
                    path={route.path}
                    element={<route.Component/>}>
                </Route>
            )}
            <Route path='*' element={<Navigate to={'/'} replace/>}></Route>
        </Routes>
    )
}