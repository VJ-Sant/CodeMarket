import { useEffect, useState, FormEvent } from 'react';
import api from '../api/api';
import { Category } from '../types';

export default function AdminCategoriesPage() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    const response = await api.get<Category[]>('/categories');
    setCategories(response.data);
  };

  const createCategory = async (event: FormEvent) => {
    event.preventDefault();
    if (!name.trim()) {
      return;
    }
    await api.post('/categories', { name, description });
    setName('');
    setDescription('');
    fetchCategories();
  };

  const deleteCategory = async (categoryId: number) => {
    await api.delete(`/categories/${categoryId}`);
    fetchCategories();
  };

  return (
    <div>
      <h2>Admin Categories</h2>
      <div className="row mb-4">
        <div className="col-md-6">
          <form onSubmit={createCategory} className="card card-body">
            <div className="mb-3">
              <label className="form-label">Category Name</label>
              <input className="form-control" value={name} onChange={(e) => setName(e.target.value)} required />
            </div>
            <div className="mb-3">
              <label className="form-label">Description</label>
              <textarea className="form-control" value={description} onChange={(e) => setDescription(e.target.value)} rows={3} />
            </div>
            <button type="submit" className="btn btn-primary">
              Create Category
            </button>
          </form>
        </div>
      </div>
      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              <th>Name</th>
              <th>Description</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {categories.map((category) => (
              <tr key={category.id}>
                <td>{category.name}</td>
                <td>{category.description}</td>
                <td>
                  <button className="btn btn-sm btn-outline-danger" onClick={() => deleteCategory(category.id)}>
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
