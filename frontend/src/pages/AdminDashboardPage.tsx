import { useEffect, useState } from 'react';
import api from '../api/api';
import { User } from '../types';

export default function AdminDashboardPage() {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    const response = await api.get<User[]>('/admin/users');
    setUsers(response.data);
  };

  const toggleAdmin = async (userId: number, isAdmin: boolean) => {
    if (isAdmin) {
      await api.post(`/admin/users/${userId}/revoke-admin`);
    } else {
      await api.post(`/admin/users/${userId}/grant-admin`);
    }
    fetchUsers();
  };

  return (
    <div>
      <h2>Admin Dashboard</h2>
      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              <th>User</th>
              <th>Email</th>
              <th>Role</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>{user.role}</td>
                <td>
                  <button className="btn btn-sm btn-outline-primary" onClick={() => toggleAdmin(user.id, user.role === 'ADMIN')}>
                    {user.role === 'ADMIN' ? 'Revoke Admin' : 'Grant Admin'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
