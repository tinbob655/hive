import React, {lazy} from 'react';
import {Route, Routes} from "react-router";

//import all pages lazily
const Home = lazy(() => import("./pages/home/Home.tsx"));

const pages: [string, React.ComponentType][] = [
    ['', Home]
];

export default function AllRoutes(): React.ReactElement {

    return (
        <Routes>
            {pages.map(([path, Page]) => (
                <Route key={path} path={`/${path}`} element={<Page/>} />
            ))}
        </Routes>
    )
}