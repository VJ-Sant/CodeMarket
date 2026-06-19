import { useEffect, useState, ChangeEvent, FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../api/api';
import { Project } from '../types';

interface Category {
  id: number;
  name: string;
  description: string;
}

const initialFormState = {
  projectName: '',
  projectDescription: '',
  technologyStack: '',
  difficultyLevel: '',
  projectPrice: 0,
  thumbnailUrl: '',
  categoryId: 0
};

export default function ProjectFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState(initialFormState);
  const [categories, setCategories] = useState<Category[]>([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadCategories();

    if (id) {
      loadProject(Number(id));
    }
  }, [id]);

  const loadCategories = async () => {
    try {
      const response = await api.get('/categories');
      setCategories(response.data);
    } catch (error) {
      console.error('Failed to load categories', error);
    }
  };

  const loadProject = async (projectId: number) => {
    try {
      const response = await api.get<Project>(`/projects/${projectId}`);
      const project = response.data;

      setForm({
        projectName: project.projectName,
        projectDescription: project.projectDescription,
        technologyStack: project.technologyStack,
        difficultyLevel: project.difficultyLevel,
        projectPrice: project.projectPrice,
        thumbnailUrl: project.thumbnailUrl || '',
        categoryId: (project as any).categoryId || 0
      });
    } catch (error) {
      console.error(error);
    }
  };

  const handleChange = (
    e: ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >
  ) => {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]:
        name === 'projectPrice' || name === 'categoryId'
          ? Number(value)
          : value
    }));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    console.log('Submitting:', form);

    try {
      if (id) {
        await api.put(`/projects/${id}`, form);
        setMessage('Project updated successfully.');
      } else {
        await api.post('/projects', form);
        setMessage('Project created successfully.');
      }

      navigate('/seller/projects');
    } catch (error) {
      console.error(error);
      alert('Failed to save project');
    }
  };

  return (
    <div className="container mt-4">
      <h2>{id ? 'Edit Project' : 'Create Project'}</h2>

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Project Name</label>
          <input
            className="form-control"
            name="projectName"
            value={form.projectName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Description</label>
          <textarea
            className="form-control"
            name="projectDescription"
            value={form.projectDescription}
            onChange={handleChange}
            rows={4}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Category</label>
          <select
            className="form-select"
            name="categoryId"
            value={form.categoryId}
            onChange={handleChange}
            required
          >
            <option value={0}>Select Category</option>

            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Technology Stack</label>
          <input
            className="form-control"
            name="technologyStack"
            value={form.technologyStack}
            onChange={handleChange}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Difficulty Level</label>
          <input
            className="form-control"
            name="difficultyLevel"
            value={form.difficultyLevel}
            onChange={handleChange}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Price</label>
          <input
            className="form-control"
            type="number"
            name="projectPrice"
            value={form.projectPrice}
            onChange={handleChange}
            min="0"
            step="0.01"
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Thumbnail URL</label>
          <input
            className="form-control"
            name="thumbnailUrl"
            value={form.thumbnailUrl}
            onChange={handleChange}
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Save Project
        </button>
      </form>

      {message && (
        <div className="alert alert-success mt-3">
          {message}
        </div>
      )}
    </div>
  );
}