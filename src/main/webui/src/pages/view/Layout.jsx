import { Outlet, Link } from "react-router-dom";

const Layout = () => {
  return (
      <>
        <nav className="border-2 h-10">
          <ul className="flex flex-row justify-center items-center gap-20">
            <li>
              <Link to="/">Home</Link>
            </li>
            <li>
              <Link to="/contact">Contact</Link>
            </li>
            <li>
              <Link to="/minio">Minio</Link>
            </li>
              <li>
                  <Link to="/bucket">Bucket</Link>
              </li>
          </ul>
        </nav>

        <Outlet/>
      </>
  )
};

export default Layout;