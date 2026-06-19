import { useEffect, useState } from 'react';
import api from '../api/api';
import { User } from '../types';

export default function AdminUsersPage() {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    const response = await api.get<User[]>('/admin/users');
    setUsers(response.data);
  };

  const toggleUserStatus = async (user: User) => {
    const endpoint = user.enabled ? `/admin/users/${user.id}/disable` : `/admin/users/${user.id}/enable`;
    await api.post(endpoint);
    fetchUsers();
  };

  const deleteUser = async (userId: number) => {
    await api.delete(`/admin/users/${userId}`);
    fetchUsers();
  };

  return (
    <div>
      <h2>Admin Users</h2>
      <div className="table-responsive">
        <table className="table table-hover">
          <thead>
            <tr>
              <th>Username</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Role</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>{user.phone}</td>
                <td>{user.role}</td>
                <td>{user.enabled ? 'Enabled' : 'Disabled'}</td>
                <td>
                  <button className="btn btn-sm btn-outline-secondary me-2" onClick={() => toggleUserStatus(user)}>
                    {user.enabled ? 'Disable' : 'Enable'}
                  </button>
                  <button className="btn btn-sm btn-outline-danger" onClick={() => deleteUser(user.id)}>
                    Delete
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
