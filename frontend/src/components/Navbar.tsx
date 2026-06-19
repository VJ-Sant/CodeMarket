import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">
          CodeMarket
        </Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <Link className="nav-link" to="/">
                Marketplace
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/">
                Categories
              </Link>
            </li>
          </ul>
          <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
            {user ? (
              <>
                <li className="nav-item dropdown">
                  <button className="btn btn-secondary dropdown-toggle me-2" data-bs-toggle="dropdown">
                    {user.username}
                  </button>
                  <ul className="dropdown-menu dropdown-menu-end">
                    <li>
                      <Link className="dropdown-item" to="/dashboard/profile">
                        Profile
                      </Link>
                    </li>
                    <li>
                      <Link className="dropdown-item" to="/dashboard/favorites">
                        Favorites
                      </Link>
                    </li>
                    <li>
                      <Link className="dropdown-item" to="/dashboard/purchases">
                        Purchases
                      </Link>
                    </li>
                    {user.role === 'SELLER' || user.role === 'ADMIN' ? (
                      <>
                        <li>
                          <hr className="dropdown-divider" />
                        </li>
                        <li>
                          <Link className="dropdown-item" to="/seller/projects">
                            My Projects
                          </Link>
                        </li>
                        <li>
                          <Link className="dropdown-item" to="/seller/projects/new">
                            Create Project
                          </Link>
                        </li>
                      </>
                    ) : null}
                    {user.role === 'ADMIN' ? (
                      <>
                        <li>
                          <hr className="dropdown-divider" />
                        </li>
                        <li>
                          <Link className="dropdown-item" to="/admin/users">
                            Admin Users
                          </Link>
                        </li>
                        <li>
                          <Link className="dropdown-item" to="/admin/categories">
                            Admin Categories
                          </Link>
                        </li>
                        <li>
                          <Link className="dropdown-item" to="/admin/projects">
                            Admin Projects
                          </Link>
                        </li>
                      </>
                    ) : null}
                    <li>
                      <hr className="dropdown-divider" />
                    </li>
                    <li>
                      <button className="dropdown-item" onClick={logout}>
                        Logout
                      </button>
                    </li>
                  </ul>
                </li>
              </>
            ) : (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/login">
                    Login
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/register">
                    Register
                  </Link>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}
