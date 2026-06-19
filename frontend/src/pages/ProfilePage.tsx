import { useEffect, useState, FormEvent } from 'react';
import api from '../api/api';
import { UserProfile } from '../types';

export default function ProfilePage() {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [form, setForm] = useState({ username: '', email: '', phone: '' });
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    const response = await api.get<UserProfile>('/users/me');
    setProfile(response.data);
    setForm({ username: response.data.username, email: response.data.email, phone: response.data.phone });
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    await api.put<UserProfile>('/users/me', form);
    setMessage('Profile updated successfully');
    fetchProfile();
  };

  return (
    <div className="row justify-content-center">
      <div className="col-md-8">
        <div className="card p-4">
          <h2>Profile</h2>
          {message ? <div className="alert alert-success">{message}</div> : null}
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">Username</label>
              <input className="form-control" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} />
            </div>
            <div className="mb-3">
              <label className="form-label">Email</label>
              <input type="email" className="form-control" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
            </div>
            <div className="mb-3">
              <label className="form-label">Phone</label>
              <input className="form-control" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
            </div>
            <button className="btn btn-primary">Save Changes</button>
          </form>
        </div>
      </div>
    </div>
  );
}
