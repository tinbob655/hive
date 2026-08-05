import { StrictMode, Suspense } from 'react'
import { createRoot } from 'react-dom/client'
import {BrowserRouter} from "react-router";
import './scss/index.scss';
import Header from "./components/header/Header.tsx";
import AllRoutes from "./AllRoutes.tsx";
import Footer from "./components/footer/Footer.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>

      <header>
        <Header/>
      </header>

      <Suspense fallback={<h1>Loading page...</h1>}>
        <AllRoutes/>
      </Suspense>

      <footer>
        <Footer/>
      </footer>

    </BrowserRouter>
  </StrictMode>,
)
