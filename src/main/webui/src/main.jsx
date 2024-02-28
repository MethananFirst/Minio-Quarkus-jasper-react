import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./pages/view/Layout.jsx";
import Home from "./pages/view/Home.jsx";
import Contact from "./pages/view/Contact.jsx";
import NoPage from "./pages/NoPage";
import Bucket from "./pages/view/Bucket.jsx";
import ListFile from "./pages/view/ListFile.jsx";
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";


const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
  },
  {
    path: "/bucket",
    element: <Bucket/>,
  },
  {
    path: "/file/:bucket",
    element: <ListFile/>,
  },
  {
    path: "/contact",
    element: <Contact/>,
  },
  {
    path: "/nopage",
    element: <NoPage/>,
  }
  // ,
  // {
  //   path: "/about",
  //   element: <About/>,
  // },
  // {
  //   path: "/service",
  //   element: <Service/>,
  // },
  // {
  //   path: "/profile",
  //   element: <Profile/>,
  // },
  // {
  //   path: "/obj-browse",
  //   element: <ObjectBrowse/>,
  // },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
      <RouterProvider router={router} />
    </React.StrictMode>,
)
